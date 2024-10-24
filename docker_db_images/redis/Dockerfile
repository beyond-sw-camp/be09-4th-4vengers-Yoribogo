# 베이스 이미지로 Redis를 사용 (alpine 기반 경량 이미지)
FROM redis:alpine

# Redis 설정 파일 복사
COPY redis.conf /usr/local/etc/redis/redis.conf

# Redis 설정 적용하여 실행
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
