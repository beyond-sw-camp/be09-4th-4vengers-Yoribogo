import sys

sys.stdout.reconfigure(encoding="utf-8")  # 한글 출력을 위해 추가

# 라이브러리 설치 후 진행해주세요
import requests
from sqlalchemy import (
    create_engine,
    Column,
    String,
    Integer,
    Text,
    ForeignKey,
    DateTime,
)
from sqlalchemy.orm import declarative_base, sessionmaker
from datetime import datetime

# 데이터베이스 설정, 본인 계정과 비번 입력하세요
DATABASE_URL = "mysql+mysqldb://yoribogo:yoribogo@localhost/yoribogodb"

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()

Base = declarative_base()


# 테이블 정의
class Recipe(Base):
    __tablename__ = "recipe"

    recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    menu_name = Column(String(255), nullable=False)
    menu_ingredient = Column(Text, nullable=False)
    menu_image = Column(Text)
    menu_type = Column(String(255), default="PUBLIC")
    user_id = Column(Integer, nullable=False)


class PublicDataRecipe(Base):
    __tablename__ = "public_data_recipe"

    public_data_recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    public_data_menu_name = Column(String(255), nullable=False)
    public_data_menu_ingredient = Column(Text, nullable=False)
    public_data_menu_image = Column(Text)
    recipe_id = Column(Integer, ForeignKey("recipe.recipe_id"), nullable=False)


class RecipeManual(Base):
    __tablename__ = "recipe_manual"

    recipe_manual_id = Column(Integer, primary_key=True, autoincrement=True)
    recipe_manual_step = Column(Integer, nullable=False)  # 단계 추가
    manual_content = Column(Text, nullable=False)
    manual_menu_image = Column(Text)
    recipe_id = Column(Integer, ForeignKey("recipe.recipe_id"), nullable=False)


# 티어 테이블
class Tier(Base):
    __tablename__ = "tier"

    tier_id = Column(Integer, primary_key=True, autoincrement=True)
    tier_name = Column(String(255), nullable=False)
    tier_criteria = Column(Integer, nullable=False)


# 사용자 테이블
class User(Base):
    __tablename__ = "user"

    user_id = Column(Integer, primary_key=True, autoincrement=True)
    user_name = Column(String(255), nullable=False)
    password = Column(String(255), nullable=False)
    nickname = Column(String(255), nullable=False)
    email = Column(String(255), nullable=False)
    profile_image = Column(Text)
    created_at = Column(DateTime, default=datetime.utcnow)
    signup_path = Column(String(255), nullable=False)
    user_identifier = Column(String(255), nullable=False)
    user_auth_id = Column(String(255), nullable=False)
    user_status = Column(String(255), nullable=False, default="ACTIVE")
    user_role = Column(String(255), nullable=False)
    user_likes = Column(Integer, default=0)
    tier_id = Column(Integer, ForeignKey("tier.tier_id"))
    accept_status = Column(String(255), nullable=False, default="N")


# 레시피가 이미 있는지 확인하는 함수
def find_recipe_by_name(menu_name):
    return session.query(Recipe).filter_by(menu_name=menu_name).first()


# 데이터 삽입 함수
def insert_data(data):
    # 기본 사용자를 설정합니다 (예: user_id=1이 실제로 존재하는지 확인)
    default_user = session.query(User).filter_by(user_name="조찬국").first()

    if not default_user:
        print("기본 사용자가 존재하지 않습니다. 레시피를 삽입할 수 없습니다.")
        return

    for item in data:
        if item["RCP_PAT2"] != "후식":  # '후식' 제외
            existing_recipe = find_recipe_by_name(item["RCP_NM"])
            if not existing_recipe:
                recipe = Recipe(
                    menu_name=item["RCP_NM"],
                    menu_ingredient=item["RCP_PARTS_DTLS"],
                    menu_image=item["ATT_FILE_NO_MAIN"],
                    user_id=default_user.user_id,  # 기본 사용자로 설정
                )
                session.add(recipe)
                session.commit()

                public_data_recipe = PublicDataRecipe(
                    public_data_menu_name=item["RCP_NM"],
                    public_data_menu_ingredient=item["RCP_PARTS_DTLS"],
                    public_data_menu_image=item["ATT_FILE_NO_MAIN"],
                    recipe_id=recipe.recipe_id,
                )
                session.add(public_data_recipe)

                for i in range(1, 21):
                    manual_key = f"MANUAL{i:02}"
                    manual_img_key = f"MANUAL_IMG{i:02}"
                    if item[manual_key]:
                        recipe_manual = RecipeManual(
                            recipe_manual_step=i,  # 단계 추가
                            manual_content=item[manual_key],
                            manual_menu_image=item[manual_img_key],
                            recipe_id=recipe.recipe_id,
                        )
                        session.add(recipe_manual)
                session.commit()
            else:
                print(
                    f"레시피 '{item['RCP_NM']}'가 이미 존재합니다. 삽입하지 않습니다."
                )


# 티어 데이터 삽입 함수
def insert_tier_data():
    tiers = [
        Tier(tier_name="브론즈", tier_criteria=0),
        Tier(tier_name="실버", tier_criteria=1),
        Tier(tier_name="골드", tier_criteria=5),
        Tier(tier_name="플레티넘", tier_criteria=10),
        Tier(tier_name="다이아", tier_criteria=20),
        Tier(tier_name="챌린저", tier_criteria=50),
    ]
    session.add_all(tiers)
    session.commit()


# 사용자 데이터 삽입 함수
def insert_user_data():
    # 티어 기준에 따른 자동 할당 로직
    def get_tier_id(likes):
        tier = (
            session.query(Tier)
            .filter(Tier.tier_criteria <= likes)
            .order_by(Tier.tier_criteria.desc())
            .first()
        )
        return tier.tier_id if tier else None

    users = [
        # 관리자
        User(
            user_name="조찬국",
            password="1234",
            nickname="조찬국",
            email="alice@example.com",
            profile_image=None,
            created_at="2023-08-01 10:30:00",
            signup_path="ADMIN",
            user_identifier="ADMIN_changuk1234",
            user_auth_id="changuk1234",
            user_status="ACTIVE",
            user_role="ADMIN",
            accept_status="Y",
        ),
        User(
            user_name="김범기",
            password="password123",
            nickname="princess_d",
            email="diana@example.com",
            profile_image=None,
            created_at="2023-08-04 09:15:00",
            signup_path="ADMIN",
            user_identifier="ADMIN_diana789",
            user_auth_id="diana789",
            user_status="ACTIVE",
            user_role="ADMIN",
            accept_status="Y",
        ),
        # 일반 회원
        User(
            user_name="김석민",
            password="password123",
            nickname="bob_the_builder",
            email="bob@example.com",
            profile_image=None,
            created_at="2023-08-02 11:45:00",
            signup_path="NAVER",
            user_identifier="NAVER_987654321",
            user_auth_id="987654321",
            user_status="ACTIVE",
            user_role="ENTERPRISE",
            accept_status="Y",
            tier_id=get_tier_id(25),
            user_likes=25,
        ),  # 다이아
        User(
            user_name="조국찬",
            password="password456",
            nickname="조국찬",
            email="changuk0308@naver.com",
            profile_image=None,
            created_at="2023-08-03 12:00:00",
            signup_path="NORMAL",
            user_identifier="NORMAL_abc123xyz",
            user_auth_id="abc123xyz",
            user_status="ACTIVE",
            user_role="ENTERPRISE",
            accept_status="Y",
            tier_id=get_tier_id(15),
            user_likes=15,
        ),  # 플레티넘
        User(
            user_name="이소라",
            password="password789",
            nickname="eve_123",
            email="eve@example.com",
            profile_image=None,
            created_at="2023-08-05 14:20:00",
            signup_path="KAKAO",
            user_identifier="KAKAO_kakao98765",
            user_auth_id="kakao98765",
            user_status="ACTIVE",
            user_role="ENTERPRISE",
            accept_status="Y",
            tier_id=get_tier_id(8),
            user_likes=8,
        ),  # 골드
        User(
            user_name="박민수",
            password="password123",
            nickname="minsu_p",
            email="minsu@example.com",
            profile_image=None,
            created_at="2023-09-01 10:00:00",
            signup_path="NORMAL",
            user_identifier="NORMAL_minsu123",
            user_auth_id="minsu123",
            user_status="ACTIVE",
            user_role="ENTERPRISE",
            accept_status="Y",
            tier_id=get_tier_id(3),
            user_likes=3,
        ),  # 실버
        User(
            user_name="이정현",
            password="password456",
            nickname="jung_h",
            email="jung@example.com",
            profile_image=None,
            created_at="2023-09-05 09:30:00",
            signup_path="KAKAO",
            user_identifier="KAKAO_jung123",
            user_auth_id="jung123",
            user_status="ACTIVE",
            user_role="ENTERPRISE",
            accept_status="Y",
            tier_id=get_tier_id(0),
            user_likes=0,
        ),  # 브론즈
    ]
    session.add_all(users)
    session.commit()


# API로 데이터 가져오기
def fetch_data():
    API_URL_1 = "https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/1/1000"
    API_URL_2 = "https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/1001/1124"

    response_1 = requests.get(API_URL_1)
    response_2 = requests.get(API_URL_2)

    if response_1.status_code == 200:
        data1 = response_1.json().get("COOKRCP01", {}).get("row", [])
        print(f"첫 번째 데이터 수: {len(data1)}")
        insert_data(data1)
    else:
        print(f"첫 번째 API 호출 실패: {response_1.status_code}")

    if response_2.status_code == 200:
        data2 = response_2.json().get("COOKRCP01", {}).get("row", [])
        print(f"두 번째 데이터 수: {len(data2)}")
        insert_data(data2)
    else:
        print(f"두 번째 API 호출 실패: {response_2.status_code}")


# 데이터 가져오기 실행
fetch_data()

# 사용자와 티어 데이터 삽입
insert_tier_data()
insert_user_data()

print("데이터 삽입 완료!")
