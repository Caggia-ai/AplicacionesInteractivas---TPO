# Marketplace API — TPO Aplicaciones Interactivas

API REST de un marketplace desarrollada con Spring Boot para el TP de Aplicaciones Interactivas (UADE). Permite gestionar usuarios, productos, categorías, carritos de compra, órdenes e imágenes de productos, con autenticación mediante JWT.

## Tecnologías

- Java 21
- Spring Boot (Web, Data JPA, Security, Actuator)
- MySQL
- JWT (JJWT) para autenticación
- Lombok
- Maven

## Requisitos previos

- JDK 21
- Maven (o usar el wrapper `./mvnw`)
- MySQL corriendo localmente

## Configuración

1. Crear la base de datos en MySQL:
```sql
   CREATE DATABASE marketplace;
```
2. Revisar/completar `src/main/resources/application.properties` con las credenciales de tu base (usuario y contraseña de MySQL).
3. La clave del JWT se puede definir por variable de entorno `JWT_SECRET` (si no se define, se usa una clave por defecto solo para desarrollo local).

## Cómo correr el proyecto

```bash
./mvnw spring-boot:run
```

La aplicación levanta por defecto en `http://localhost:8080`.

## Autenticación

Todos los endpoints (salvo el registro y el login) requieren un token JWT.

- `POST /api/v1/auth/register` — registra un usuario nuevo.
- `POST /api/v1/auth/login` — devuelve un `accessToken` (JWT).

El token se envía en cada request en el header:
```
Authorization: Bearer <token>
```

## Endpoints principales

### Usuarios
- `GET /users` — lista usuarios (paginado)
- `GET /users/{userId}` — usuario por id
- `POST /users` — crear usuario
- `PATCH /users/{userId}` — editar usuario

### Productos
- `GET /products` — lista productos (paginado)
- `GET /products/{productId}` — producto por id
- `POST /products` — crear producto
- `PATCH /products/{id}` — editar producto
- `DELETE /products/{id}` — baja lógica de producto

### Categorías
- `GET /categories` — lista categorías
- `GET /categories/{categoryId}` — categoría por id
- `POST /categories` — crear categoría

### Carrito
- `GET /carts/user/{userId}` — carrito de un usuario
- `DELETE /carts/user/{userId}/clear` — vaciar carrito

### Ítems del carrito
- `POST /cartItems/user/{userId}` — agregar producto al carrito
- `DELETE /cartItems/user/{userId}/product/{productId}` — quitar una unidad del carrito

### Órdenes
- `POST /orders/checkout/user/{userId}` — generar una orden a partir del carrito
- `GET /orders/user/{userId}` — órdenes de un usuario
- `GET /orders/{orderId}` — orden por id

### Ítems de orden
- `GET /orderItems/order/{orderId}` — ítems de una orden
- `GET /orderItems/{itemId}` — ítem por id

### Imágenes
- `POST /images` — subir imagen de un producto
- `GET /images?id={id}` — obtener imagen (en base64)
- `DELETE /images?id={id}` — eliminar imagen

## Estructura del proyecto

```
src/main/java/com/uade/tpo/marketplace
├── controllers        # Controladores REST
│   └── config         # Configuración de seguridad y JWT
├── entity              # Entidades JPA
│   └── dto             # DTOs de request/response
├── exceptions          # Excepciones personalizadas
├── repository          # Repositorios JPA
└── service             # Lógica de negocio
```

## Integrantes

- Lucia Gabian
- Nicolas Caggia
- Francisco Rodriguez

## Materia

Aplicaciones Interactivas — UADE
