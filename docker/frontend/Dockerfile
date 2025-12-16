FROM nginx:latest

COPY frontend/nginx.conf /etc/nginx/nginx.conf
COPY frontend/server_certificates.sh /server_certificates.sh

RUN chmod +x /server_certificates.sh

ENTRYPOINT ["/server_certificates.sh"]

CMD ["nginx", "-g", "daemon off;"]