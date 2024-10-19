package com.promani;

import java.util.Scanner;
import com.promani.db.DatabaseConnection;
import com.promani.model.Producto;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("====== Menú CRUD de Productos ======");
            System.out.println("1. Crear producto");
            System.out.println("2. Leer productos");
            System.out.println("3. Actualizar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    crearProducto();
                    break;
                    
                case 2:
                    leerProductos();
                    break;

                case 3:
                    actualizarProducto();
                    break;

                case 4:
                    eliminarProducto();
                    break;

                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        } while (opcion != 5);

        scanner.close();
    }

    public static void crearProducto() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Crear Nuevo Producto =====");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("ingredientes del producto: ");
        String ingredientes = scanner.nextLine();

        System.out.print("Precio del producto: ");
        double precio = scanner.nextDouble();

        System.out.print("Cantidad del producto: ");
        int cantidad = scanner.nextInt();

        Producto nuevoProducto = new Producto(0, nombre, ingredientes, precio, cantidad);

        guardarProductoEnBD(nuevoProducto);
    }

    public static void guardarProductoEnBD(Producto producto) {
        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            try {
                String query = "INSERT INTO Productos (nombre, ingredientes, precio, cantidad) VALUES (?, ?, ?, ?)";
                var statement = connection.prepareStatement(query);
                statement.setString(1, producto.getNombre());
                statement.setString(2, producto.getIngredientes());
                statement.setDouble(3, producto.getPrecio());
                statement.setInt(4, producto.getCantidad());

                int filasInsertadas = statement.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("¡Producto creado exitosamente!");
                }

                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al insertar el producto: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo establecer la conexión con la base de datos.");
        }
    }
    
    public static void leerProductos() {
        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            try {
                String query = "SELECT * FROM Productos";
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(query);

                System.out.println("===== Lista de Productos =====");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    String ingredientes = resultSet.getString("ingredientes");
                    double precio = resultSet.getDouble("precio");
                    int cantidad = resultSet.getInt("cantidad");

                    System.out.println("ID: " + id);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Ingredientes: " + ingredientes);
                    System.out.println("Precio: $" + precio);
                    System.out.println("Cantidad: " + cantidad);
                    System.out.println("------------------------------");
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al leer los productos: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo establecer la conexión con la base de datos.");
        }
    }
    
    public static void actualizarProducto() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Actualizar Producto =====");
        System.out.print("Ingresa el ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Nuevo nombre del producto: ");
        String nuevoNombre = scanner.nextLine();

        System.out.print("Nuevo ingrediente del producto: ");
        String nuevoingrediente = scanner.nextLine();

        System.out.print("Nuevo precio del producto: ");
        double nuevoPrecio = scanner.nextDouble();

        System.out.print("Nueva cantidad del producto: ");
        int nuevaCantidad = scanner.nextInt();

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            try {
                String query = "UPDATE Productos SET nombre = ?, ingredientes = ?, precio = ?, cantidad = ? WHERE id = ?";
                var statement = connection.prepareStatement(query);
                statement.setString(1, nuevoNombre);
                statement.setString(2, nuevoingrediente);
                statement.setDouble(3, nuevoPrecio);
                statement.setInt(4, nuevaCantidad);
                statement.setInt(5, id);

                int filasActualizadas = statement.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("¡Producto actualizado exitosamente!");
                } else {
                    System.out.println("No se encontró el producto con el ID especificado.");
                }

                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al actualizar el producto: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo establecer la conexión con la base de datos.");
        }
    }
    
    public static void eliminarProducto() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Eliminar Producto =====");
        System.out.print("Ingresa el ID del producto a eliminar: ");
        int id = scanner.nextInt();

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            try {
                String query = "DELETE FROM Productos WHERE id = ?";
                var statement = connection.prepareStatement(query);
                statement.setInt(1, id);

                int filasEliminadas = statement.executeUpdate();
                if (filasEliminadas > 0) {
                    System.out.println("¡Producto eliminado exitosamente!");
                } else {
                    System.out.println("No se encontró el producto con el ID especificado.");
                }

                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Error al eliminar el producto: " + e.getMessage());
            }
        } else {
            System.out.println("No se pudo establecer la conexión con la base de datos.");
        }
    }




}

