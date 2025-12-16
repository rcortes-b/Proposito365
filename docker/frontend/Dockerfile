FROM nginx:latest

RUN mkdir -p /usr/share/nginx/propositos

COPY frontend/ /usr/share/nginx/propositos/
COPY docker/frontend/nginx.conf /etc/nginx/nginx.conf
COPY docker/frontend/server_certificates.sh /server_certificates.sh

RUN chmod +x /server_certificates.sh

ENTRYPOINT ["/server_certificates.sh"]

CMD ["nginx", "-g", "daemon off;"]