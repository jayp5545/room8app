FROM node:20

WORKDIR /usr/src/app

COPY . ./

RUN npm install -g pnpm
RUN pnpm i

CMD [ "pnpm", "run", "dev"]