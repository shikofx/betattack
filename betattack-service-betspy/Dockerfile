# 1. Build image 
# docker build --build-arg APP_DIR=/app -t shikofx/betcity-spy:V1 .

# 2. Run image 
# docker run -p 2525:3000 -d --name betcity-spy shikofx/betcity-spy:V1

# 3. Check status
# docker ps

# 4. Check container status
# docker container logs <container name>
# docker container stats <container name>
FROM node:latest

# To run Headful mode, you will need to have a display, which is not present in a server. 
# To avoid this, we will use Xvfb, and create a fake display, so the chrome will think there is a display and run properly. 
# So we just need to install Xvfb and Puppeteer related dependencies.
RUN apt-get update && apt-get install -yq gconf-service libasound2 libatk1.0-0 libcups2 libc6 libcairo2 libcups2 libdbus-1-3 libexpat1 libfontconfig1 libgcc1 libgconf-2-4 libgdk-pixbuf2.0-0 libglib2.0-0 libgtk-3-0 libnspr4 libpango-1.0-0 libpangocairo-1.0-0 libstdc++6 libx11-6 libx11-xcb1 libxcb1 libxcomposite1 libxcursor1 libxdamage1 libxext6 libxfixes3 libxi6 libxrandr2 libxrender1 libxss1 libxtst6 ca-certificates fonts-liberation libappindicator1 libnss3 lsb-release xdg-utils wget x11vnc x11-xkb-utils xfonts-100dpi xfonts-75dpi xfonts-scalable xfonts-cyrillic x11-apps xvfb

# Создаем дирректорию app
ARG APP_DIR=app

RUN mkdir -p ${APP_DIR}

WORKDIR ${APP_DIR}

# Установим зависимость приложения
# * - используется символ подстановки для копирования
COPY package*.json ./

RUN npm install

# в продакшене использовать
# RUN npm install --production 

# скопировать исходники приложения
COPY . .

# RUN npm run build:production
# Удаление папки с npm-модулями
# RUN rm -rf node_modules

EXPOSE 3000

CMD [ "node", "src/com/pkt/betattack/service/betspy/index.js" ]