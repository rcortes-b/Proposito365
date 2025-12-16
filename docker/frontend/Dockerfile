FROM node:20-alpine AS builder
WORKDIR /app
COPY frontend/. .
#COPY frontend/package*.json .
#RUN npm ci
RUN npm install
RUN npm run build

####

FROM nginx:latest

RUN mkdir -p /usr/share/nginx/propositos

COPY --from=builder /app/dist /usr/share/nginx/propositos/
COPY docker/frontend/nginx.conf /etc/nginx/nginx.conf
COPY docker/frontend/server_certificates.sh /server_certificates.sh
COPY docker/frontend/init.sh /init.sh

EXPOSE 80 443

RUN chmod +x /init.sh

ENTRYPOINT ["/init.sh"]