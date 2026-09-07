# Marketplace API — TPO Aplicaciones Interactivas

API REST de un marketplace desarrollada con Spring Boot para el TP de Aplicaciones Interactivas (UADE). Permite gestionar usuarios, productos, categorías, carritos de compra, órdenes e imágenes de productos, con autenticación mediante JWT.

## Tecnologías

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Security, Actuator)
- MySQL
- JWT (JJWT 0.12.6) para autenticación
- Lombok
- Maven

## Requisitos previos

- JDK 21
- Maven (o usar el wrapper `./mvnw`)
- MySQL corriendo localmente

## Configuración

1. Crear la base de datos en MySQL (la URL de conexión no crea la base sola):
```sql
   CREATE DATABASE marketplace;
```
2. Revisar/completar `src/main/resources/application.properties` con las credenciales de tu base (usuario y contraseña de MySQL).
3. La clave y expiración del JWT se pueden sobreescribir por variable de entorno (si no se definen, se usan valores por defecto solo para desarrollo local):
   - `JWT_SECRET`
   - `JWT_EXPIRATION` (en milisegundos)
4. (Opcional) Para que se cree automáticamente un usuario ADMIN al levantar la app, definir estas variables de entorno antes de arrancar:
   - `APPLICATION_SECURITY_ADMIN_EMAIL`
   - `APPLICATION_SECURITY_ADMIN_PASSWORD`

   Si no se definen, no se crea ningún admin por defecto y hay que promover un usuario a `ADMIN` a mano (por ejemplo directo en la base, ya que solo un ADMIN puede cambiar roles vía la API).

> **Importante:** `spring.jpa.hibernate.ddl-auto=create-drop` está seteado en `application.properties`, lo que significa que **el esquema se borra y se recrea en cada reinicio de la app** (se pierden todos los datos). Está bien para desarrollo/demo del TPO, pero no usar así en un entorno real.

## Cómo correr el proyecto

```bash
./mvnw spring-boot:run
```

La aplicación levanta por defecto en `http://localhost:8080`.

## Autenticación

Todos los endpoints, salvo registro, login y los marcados como públicos más abajo, requieren un token JWT.

- `POST /api/v1/auth/register` — registra un usuario nuevo (`username`, `name`, `surname`, `email`, `password`, `role` opcional — si no se manda, queda `BUYER`). Devuelve `accessToken`.
- `POST /api/v1/auth/login` — recibe `email` (acepta también `username` en ese mismo campo) y `password`. Devuelve `accessToken`.

El token se envía en cada request en el header:
```
Authorization: Bearer <token>
```

Roles disponibles: `BUYER`, `SELLER`, `ADMIN`.

## Endpoints principales

Los endpoints marcados con 🔓 son públicos (no requieren token). El resto requiere `Authorization: Bearer <token>`, y algunos además requieren un rol específico (marcado entre paréntesis).

### Usuarios
- `GET /users` — lista usuarios (paginado con `?page=&size=`)
- `GET /users/{userId}` — usuario por id
- `PATCH /users/{userId}` — editar perfil propio, o de cualquier usuario si sos ADMIN (cambiar el `role` de una cuenta requiere ser ADMIN)

No hay un `POST /users` directo: los usuarios se crean vía `/api/v1/auth/register`.

### Productos
- 🔓 `GET /products` — lista productos activos, paginado y con filtros opcionales (`categoryId`, `minPrice`, `maxPrice`, `keyword`, `page`, `size`)
- 🔓 `GET /products/{productId}` — producto por id
- `POST /products` (SELLER o ADMIN) — crear producto
- `PATCH /products/{id}` (dueño del producto o ADMIN) — editar producto
- `DELETE /products/{id}` (dueño del producto o ADMIN) — baja lógica del producto (no lo borra de la base, lo marca inactivo)

### Categorías
- `GET /categories` — lista categorías, paginado
- `GET /categories/{categoryId}` — categoría por id
- `POST /categories` (ADMIN)
- `PATCH /categories/{categoryId}` (ADMIN)
- `DELETE /categories/{categoryId}` (ADMIN) — falla si la categoría tiene productos asociados

### Carrito
El carrito siempre es el del usuario autenticado (se identifica por el JWT, no por un id en la URL).

- `GET /carts` — carrito del usuario logueado
- `DELETE /carts/clear` — vacía el carrito del usuario logueado

### Ítems del carrito
- `POST /cartItems` — agrega una cantidad de un producto al carrito propio (`productId`, `quantity` en el body)
- `PUT /cartItems/product/{productId}?quantity={n}` — fija la cantidad exacta de ese producto en el carrito (si `quantity` es 0 o menor, lo elimina)
- `DELETE /cartItems/product/one/{productId}` — resta una unidad del producto en el carrito (si llega a 0, lo elimina)
- `DELETE /cartItems/product/{productId}` — elimina el producto por completo del carrito

### Órdenes
- `POST /orders/checkout` — genera una orden a partir del carrito del usuario logueado (`paymentMethod`, `deliveryMethod` en el body), descuenta stock y vacía el carrito
- `GET /orders` — historial de órdenes del usuario logueado
- `GET /orders/{orderId}` — orden por id (solo si es del usuario logueado, o si es ADMIN)

### Ítems de orden
- `GET /orderItems/order/{orderId}` — ítems de una orden (solo si la orden es del usuario logueado, o ADMIN)
- `GET /orderItems/{itemId}` — ítem por id (misma validación de propiedad)

### Imágenes
- `POST /images` — sube una imagen para un producto propio (form-data: `name`, `file`, `productId`); solo el dueño del producto o un ADMIN
- `GET /images?id={id}` — obtiene la imagen codificada en Base64 (requiere login)
- 🔓 `GET /images/view/{id}` — devuelve la imagen cruda (para usar directo en `<img src="...">`)
- `DELETE /images?id={id}` — elimina una imagen (solo el dueño del producto o un ADMIN)

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

## Notas para quien siga trabajando en esto

- Varias excepciones de negocio (`Producto no encontrado`, `Sin stock suficiente`, `El usuario no tiene carrito`, etc.) son `RuntimeException` sin `@ResponseStatus`, así que hoy se devuelven como `500` genérico en vez de un `400`/`404` prolijo. Si se quiere pulir la API, conviene agregar un `@ControllerAdvice` con `@ExceptionHandler` para esos casos.
- `GET /categories` requiere estar logueado (a diferencia de `GET /products`, que es público). Si la idea es que cualquiera pueda navegar categorías sin login, falta agregarlo como `permitAll()` en `SecurityConfig`.

## Integrantes

- Lucia Gabian
- Nicolas Caggia
- Francisco Rodriguez

## Materia

Aplicaciones Interactivas — UADE
