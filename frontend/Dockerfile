# ---- Build Stage ----
FROM node:18-alpine AS build
WORKDIR /app

# Cache dependencies (only re-downloads when package files change)
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Build the React app
COPY . .
ARG REACT_APP_API_URL=/api
ENV REACT_APP_API_URL=$REACT_APP_API_URL
RUN npm run build

# ---- Runtime Stage ----
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build /app/build .
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
