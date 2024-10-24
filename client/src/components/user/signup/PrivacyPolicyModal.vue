<template>
    <div class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
        <div class="modal-header">
          <h2>개인정보 처리방침 및 이용약관</h2>
        </div>
        <div class="modal-body">
          <!-- 개인정보 처리방침 내용 -->
          <div class="policy-content">
            <p>
              요리보고(이하 ‘회사’)는 개인정보 보호법 등 관련 법령에 따라 회원의 개인정보를 보호하고, 이와 관련된 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같은 개인정보 처리방침을 수립하여 운영하고 있습니다.<br>
              <br>
              1. 개인정보의 수집 및 이용 목적<br>
              회사는 다음의 목적을 위해 개인정보를 수집 및 이용합니다. 수집한 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경될 시에는 사전에 동의를 구하겠습니다.<br>
              회원 가입 의사 확인, 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리<br>
              서비스의 제공 및 개선, 맞춤 서비스 제공<br>
              고객 문의, 불만 처리, 고지사항 전달<br>
              <br>
              2. 수집하는 개인정보의 항목<br>
              회사는 회원가입, 서비스 이용 등을 위해 아래와 같은 개인정보를 수집하고 있습니다.<br>
              필수항목: 성명, 이메일, 비밀번호, 휴대전화 번호<br>
              선택항목: [선택항목에 대한 정보]<br>
              <br>
              3. 개인정보의 보유 및 이용 기간<br>
              회사는 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의받은 개인정보 보유·이용기간 내에서 개인정보를 처리·보유합니다.<br>
              회원 탈퇴 시까지<br>
              관계 법령에 따른 보유 기간: [해당 법령 및 기간 기재]<br>
              <br>
              4. 개인정보의 제3자 제공<br>
              회사는 원칙적으로 회원의 개인정보를 외부에 제공하지 않습니다. 다만, 아래의 경우에는 예외로 합니다.<br>
              회원이 사전에 동의한 경우<br>
              법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우<br>
              <br>
              5. 개인정보의 파기 절차 및 방법<br>
              회사는 원칙적으로 개인정보 처리 목적이 달성된 경우에는 지체없이 해당 개인정보를 파기합니다. 파기의 절차, 기한 및 방법은 다음과 같습니다.<br>
              파기 절차: [파기 절차 설명]<br>
              파기 기한: 개인정보의 보유 기간이 경과된 후 지체 없이 파기<br>
              파기 방법: 전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제<br>
              <br>
              6. 개인정보 보호책임자 및 담당자의 연락처<br>
              회사는 개인정보 처리와 관련한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.<br>
              개인정보 보호책임자: 조찬국<br>
              연락처: [전화번호, 이메일]<br>
              <br>
              7. 기타<br>
              이 개인정보 처리방침은 [날짜]부터 시행됩니다. 본 방침은 법령, 정책 또는 회사 내부 방침의 변경에 따라 수정될 수 있으며, 변경 시 회사는 이를 웹사이트에 게시하거나 기타 적절한 방법으로 공지할 것입니다.
            </p>
          </div>
        </div>
        <div class="modal-footer">
          <YesNoButton type="cancel" label="뒤로가기" @click="closeModal" />
        </div>
      </div>
    </div>
  </template>
  
    
    <script setup>
    import YesNoButton from '@/components/common/YesNoButton.vue'; // YesNoButton 컴포넌트 임포트
    
    // 외부에서 받아온 이벤트 정의
    const emit = defineEmits(['close']);
    
    // 모달 닫기 함수
    const closeModal = () => {
      emit('close'); // 부모 컴포넌트에게 모달을 닫으라는 이벤트 전달
    };
    </script>
    
    <style scoped>
    /* 오류 텍스트 스타일 */
    .error-text {
      color: red;
      font-size: 1.4rem;
      margin-top: 0.2rem;
    }
    
    /* 모달 오버레이 */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 2000;
  }
  
  /* 모달 콘텐츠 */
  .modal-content {
    background-color: white;
    border-radius: 10px;
    width: 400px;
    height: 500px;
    padding: 2rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    position: relative;
    text-align: left;
    display: flex;
    flex-direction: column; /* 모달 내부를 수직으로 정렬 */
    justify-content: space-between; /* header, body, footer를 공간에 맞게 배치 */
  }
  
  /* 모달 닫기 버튼 */
  .close-btn {
    position: absolute;
    top: 1rem;
    right: 1rem;
    font-size: 3.2rem;
    opacity: 50%;
    background: none;
    border: none;
    cursor: pointer;
  }
  
  /* 모달 헤더 */
  .modal-header {
    text-align: center;
  }
  
  .modal-header h2 {
    margin-bottom: 1.5rem;
    font-size: 2rem;
    font-weight: 900;
    color: #2c3e60;
  }
  
  /* 모달 바디 */
  .modal-body {
    display: flex;
    flex-direction: column;
    flex-grow: 1; /* 남은 공간을 채우도록 설정 */
    overflow-y: auto; /* 스크롤을 위한 속성 */
    margin-bottom: 1rem;
  }
  
  /* 개인정보 처리방침 내용 스타일 */
  .policy-content {
    border: 1px solid #ccc; /* 경계선 */
    padding: 1.5rem; /* 내부 여백 */
    border-radius: 5px; /* 모서리 둥글게 */
    background-color: #f9f9f9; /* 배경색 */
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1); /* 안쪽 그림자 */
  }
  
  .policy-content p {
    font-size: 1.4rem;
    color: #525150;
    line-height: 1.8; /* 텍스트 줄 간격 */
    margin: 0; /* 텍스트 여백 제거 */
  }
  
  /* 모달 푸터 */
  .modal-footer {
    display: flex;
    justify-content: center; /* 가운데 정렬 */
    width: 100%;
  }
  
    </style>
    