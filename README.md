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
4. El usuario **ADMIN** se crea automáticamente al levantar la app, con el email y la contraseña definidos en `application.security.admin.email` y `application.security.admin.password` de `application.properties` (si ya existe, no se duplica). Se pueden sobreescribir con las variables de entorno `APPLICATION_SECURITY_ADMIN_EMAIL` y `APPLICATION_SECURITY_ADMIN_PASSWORD`. Solo un ADMIN puede cambiar roles vía la API.
5. Otras propiedades de `application.properties` que necesita el frontend:
   - `spring.web.error.include-message=always`: las respuestas de error incluyen el campo `message`, que el frontend muestra al usuario.
   - `spring.servlet.multipart.max-file-size=10MB` y `spring.servlet.multipart.max-request-size=30MB`: permiten subir fotos de más de 1 MB (por defecto Spring limita a 1 MB por archivo).
6. El backend permite pedidos (CORS) desde `http://localhost:5173`, que es donde corre el frontend en desarrollo. Si el frontend corre en otra dirección, hay que cambiarla en `SecurityConfig`.

> **Importante:** `spring.jpa.hibernate.ddl-auto=update` está seteado en `application.properties`: Hibernate crea las tablas que faltan y **conserva los datos entre reinicios**. La base `marketplace` tiene que existir antes de arrancar. Para empezar de cero se puede arrancar una vez con `ddl-auto=create` (por ejemplo, con la variable de entorno `SPRING_JPA_HIBERNATE_DDL_AUTO=create`), que **borra y recrea todas las tablas** (se pierden todos los datos); después se vuelve a arrancar normalmente.

## Cómo correr el proyecto

```bash
./mvnw spring-boot:run
```

La aplicación levanta por defecto en `http://localhost:8080`.

## Autenticación

Todos los endpoints, salvo registro, login y los marcados como públicos más abajo, requieren un token JWT.

- `POST /api/v1/auth/register` — registra un usuario nuevo (`username`, `name`, `surname`, `email`, `password`, `role` opcional — si no se manda, queda `BUYER`; no se puede registrar un `ADMIN` desde afuera). La contraseña debe tener **al menos 8 caracteres**. Devuelve `accessToken`.
- `POST /api/v1/auth/login` — recibe `email` (acepta también `username` en ese mismo campo) y `password`. Devuelve `accessToken`.

El token se envía en cada request en el header:
```
Authorization: Bearer <token>
```

Roles disponibles: `BUYER`, `SELLER`, `ADMIN`.

## Endpoints principales

Los endpoints marcados con 🔓 son públicos (no requieren token). El resto requiere `Authorization: Bearer <token>`, y algunos además requieren un rol específico (marcado entre paréntesis).

### Usuarios
- `GET /users` — lista usuarios (paginado con `?page=&size=`; sin esos parámetros devuelve todos)
- `GET /users/{userId}` — usuario por id
- `PATCH /users/{userId}` — editar perfil propio, o de cualquier usuario si sos ADMIN. Campos opcionales: `username`, `name`, `surname`, `email`, `password`, `currentPassword`, `role`. Devuelve el usuario actualizado y un token nuevo (el email es el que identifica al usuario en el JWT, así que si cambia hay que usar el token nuevo).
  - **Cambiar la contraseña:** hay que mandar también `currentPassword` (la contraseña actual de esa cuenta), que se verifica contra la guardada. La nueva debe tener **al menos 8 caracteres**.
  - Cambiar el `role` de una cuenta requiere ser ADMIN.

No hay un `POST /users` directo: los usuarios se crean vía `/api/v1/auth/register`.

### Productos
- 🔓 `GET /products` — lista productos activos, paginado y con filtros opcionales (`categoryId`, `minPrice`, `maxPrice`, `keyword`, `page`, `size`)
- 🔓 `GET /products/{productId}` — producto por id
- `GET /products/seller` — publicaciones del usuario logueado (todas las del sistema si es ADMIN), **incluidas las eliminadas**. Sin token responde 401.
- `POST /products` (SELLER o ADMIN) — crear producto (`name`, `description`, `price`, `stock`, `discount_percentage`, `id_category`)
- `PATCH /products/{id}` (dueño del producto o ADMIN) — editar producto. Campos opcionales: `name`, `description`, `price`, `stock`, `discount_percentage`, `state` (con `state: true` se vuelve a publicar un producto eliminado)
- `PUT /products/{id}` (dueño del producto o ADMIN) — **baja lógica**: no lo borra de la base, lo marca inactivo (deja de verse en el catálogo) y lo saca de los carritos. Responde 204.

### Categorías
- 🔓 `GET /categories` — lista categorías, paginado (público, para poder filtrar el catálogo sin iniciar sesión)
- 🔓 `GET /categories/{categoryId}` — categoría por id
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
- `POST /orders/checkout` — genera una orden a partir del carrito del usuario logueado (`paymentMethod`, `deliveryMethod` en el body), valida y descuenta el stock y vacía el carrito
- `GET /orders` — historial de órdenes del usuario logueado
- `GET /orders/{orderId}` — orden por id (solo si es del usuario logueado, o si es ADMIN)

### Ítems de orden
- `GET /orderItems/order/{orderId}` — ítems de una orden (solo si la orden es del usuario logueado, o ADMIN)
- `GET /orderItems/{itemId}` — ítem por id (misma validación de propiedad)

### Imágenes
- `POST /images` — sube una imagen para un producto propio (form-data: `name`, `file`, `productId`); solo el dueño del producto o un ADMIN. Un producto puede tener varias imágenes (se sube una por pedido).
- `GET /images?id={id}` — obtiene la imagen codificada en Base64 (requiere login)
- 🔓 `GET /images/view/{id}` — devuelve la imagen cruda (para usar directo en `<img src="...">`)
- `DELETE /images?id={id}` — elimina una imagen (solo el dueño del producto o un ADMIN)

## Errores

Con `spring.web.error.include-message=always`, las respuestas de error tienen la forma estándar de Spring (`timestamp`, `status`, `error`, `message`, `path`) y el campo `message` trae el motivo (por ejemplo, "No hay suficiente stock (disponible: 3).", "La contraseña actual es incorrecta." o un aviso de nombre duplicado). El frontend muestra ese texto al usuario.

Cuando Spring Security rechaza un pedido (sin token, token vencido o sin el rol necesario), el `message` es el texto interno de Spring (`Access Denied` o `Forbidden`).

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

## Decisiones y notas para quien siga trabajando en esto

- **Baja lógica de productos:** "eliminar" un producto lo desactiva (`state = false`) en lugar de borrarlo, para no romper el historial de órdenes que lo incluyen. El nombre de un producto eliminado **sigue reservado**: al crear uno nuevo, el backend rechaza un nombre que ya existe aunque esté eliminado. Así el vendedor siempre puede volver a publicar su producto sin que otro haya tomado el nombre.
- Al **modificar** el nombre de un producto (`PATCH`) no se valida que coincida con el de otro; esa validación solo se hace al crear. Es una posible mejora.
- Varias excepciones de negocio (`Producto no encontrado`, `Sin stock suficiente`, `El usuario no tiene carrito`, las de contraseña, etc.) son `RuntimeException` sin `@ResponseStatus`, así que se devuelven con estado `500` en vez de un `400`/`404` más preciso (el mensaje sí llega al cliente). Si se quiere pulir la API, conviene usar `ResponseStatusException` o un `@ControllerAdvice` con `@ExceptionHandler` para esos casos.
- Los mensajes de Spring Security ante un acceso denegado salen en inglés (`Access Denied`, `Forbidden`). Se podrían reemplazar con un `authenticationEntryPoint` y un `accessDeniedHandler` propios en `SecurityConfig`.

## Integrantes

- Lucia Gabian
- Nicolas Caggia
- Francisco Rodriguez

## Materia

Aplicaciones Interactivas — UADE
