FROM mysql:8.4

ENV MYSQL_ROOT_PASSWORD=MiClaveSegura123*
ENV MYSQL_DATABASE=currency_converter

COPY currency_mysql84.sql /docker-entrypoint-initdb.d/01_currency.sql
