#!/bin/sh

DIR_PATH="/etc/nginx/certs/"
CERT_PATH="$DIR_PATH/server.crt"
KEY_PATH="$DIR_PATH/server.key"

if [ -f "$KEY_FILE" ] || [ -f "$CERT_FILE" ]; then
    exit 1
fi

sudo mkdir -p "$CERT_DIR"

sudo openssl req -x509 -nodes -days 365 \
    -newkey rsa:2048 \
    -keyout "$KEY_FILE" \
    -out "$CERT_FILE" \
    -subj "/C=ES/ST=Valencia/L=Valencia/O=Propositos365/OU=IT/CN=localhost" # Localhost must be modified for the IP or domain!!!

sudo chmod 600 "$KEY_FILE" "$CERT_FILE"