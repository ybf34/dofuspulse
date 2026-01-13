FROM node:18-alpine as build

WORKDIR /app

COPY package*.json ./
RUN npm ci --force

COPY . .

RUN npm run build

FROM caddy:2-alpine

COPY --from=build /app/dist /srv
COPY Caddyfile /etc/caddy/Caddyfile

EXPOSE 80 443