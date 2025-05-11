FROM gradle:8.2.1-jdk17 AS build
COPY . /app
WORKDIR /app
RUN gradle installDist

FROM eclipse-temurin:17
COPY --from=build /app/build/install/mercadoautonomo /app
WORKDIR /app
CMD ["bin/mercadoautonomo"]