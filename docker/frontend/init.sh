#!/bin/sh

chmod +x /server_certificates.sh
/server_certificates.sh


nginx -g 'daemon off;'