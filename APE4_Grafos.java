import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Arista
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Grafo
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1
        // Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {
            // Crear el nodo con su id y nombre
            nodos.put(id, new Nodo(id, nombre));
            // Inicializar la lista de aristas de este nodo como vacía
            adyacencia.put(id, new ArrayList<>());

        }

        // ═══════════════════════════════════
        // TODO 2
        // Agregar arista no dirigida
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {

            // Conexión de origen hacia destino
            adyacencia.get(origen).add(new Arista(destino, peso));
            // Conexión de destino hacia origen (grafo no dirigido)
            adyacencia.get(destino).add(new Arista(origen, peso));

        }

        // ═══════════════════════════════════
        // TODO 3 — BFS
        // Ruta con menos paradas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola para recorrer niveles
            Queue<List<String>> cola = new LinkedList<>();

            // Nodos visitados
            Set<String> visitados = new HashSet<>();

            // Camino inicial
            List<String> caminoInicial = new ArrayList<>();

            // Agregar nodo inicio al camino inicial
            caminoInicial.add(inicio);

            // Agregar caminoInicial a la cola
            cola.add(caminoInicial);

            // Marcar inicio como visitado
            visitados.add(inicio);

            while (!cola.isEmpty()) {

                // Obtener el primer camino de la cola (FIFO)
                List<String> camino = cola.poll();

                // Nodo actual
                String actual = camino.get(camino.size() - 1);

                // Si llegamos al destino
                if (actual.equals(fin)) {
                    return camino;
                }

                // Recorrer vecinos
                for (Arista arista : adyacencia.get(actual)) {

                    // Verificar si el vecino NO fue visitado
                    if (!visitados.contains(arista.destino)) {

                        // Marcar vecino como visitado
                        visitados.add(arista.destino);

                        // Crear nuevo camino
                        List<String> nuevoCamino = new ArrayList<>(camino);

                        // Agregar vecino al nuevo camino
                        nuevoCamino.add(arista.destino);

                        // Agregar nuevoCamino a la cola para explorar después
                        cola.add(nuevoCamino);

                    }
                }
            }

            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — Dijkstra
        // Ruta con menor distancia
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {

            Map<String, Integer> distancias = new HashMap<>();

            Map<String, String> anteriores = new HashMap<>();

            PriorityQueue<String> cola = new PriorityQueue<>(
                    Comparator.comparingInt(
                            distancias::get));

            // Inicializar distancias
            for (String nodo : nodos.keySet()) {

                distancias.put(nodo, Integer.MAX_VALUE);

            }

            // Distancia del nodo inicio = 0 (ya estamos aquí)
            distancias.put(inicio, 0);

            // Agregar inicio a la cola de prioridad
            cola.add(inicio);

            while (!cola.isEmpty()) {

                // Obtener el nodo con menor distancia acumulada
                String actual = cola.poll();

                // Si llegamos al destino, podemos detenernos
                if (actual.equals(fin))
                    break;

                for (Arista arista : adyacencia.get(actual)) {

                    // Calcular nueva distancia: distancia actual + peso de la arista
                    int nuevaDistancia = distancias.get(actual) + arista.peso;

                    // Verificar si nuevaDistancia es MENOR que la conocida
                    if (nuevaDistancia < distancias.get(arista.destino)) {

                        // Actualizar distancia con el valor menor encontrado
                        distancias.put(arista.destino, nuevaDistancia);

                        // Guardar nodo anterior (para reconstruir el camino)
                        anteriores.put(arista.destino, actual);

                        // Agregar vecino a la cola para procesarlo
                        cola.add(arista.destino);

                    }
                }
            }

            // Reconstruir camino
            List<String> camino = new ArrayList<>();

            String actual = fin;

            while (actual != null) {

                camino.add(0, actual);

                actual = anteriores.get(actual);
            }

            return camino;
        }

        // ═══════════════════════════════════
        // Mostrar resultado
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {

            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }

            for (int i = 0; i < ruta.size(); i++) {

                String idNodo = ruta.get(i);

                Nodo nodo = nodos.get(idNodo);

                System.out.print(
                        nodo.nombre + " (" + nodo.id + ")");

                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }

    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        // NODOS
        grafo.agregarNodo("uta", "Universidad");
        grafo.agregarNodo("fisei", "FISEI");
        grafo.agregarNodo("idiomas", "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio", "Estadio");
        grafo.agregarNodo("comedor", "Comedor");

        // ARISTAS
        grafo.agregarArista("uta", "fisei", 50);
        grafo.agregarArista("fisei", "idiomas", 40);
        grafo.agregarArista("idiomas", "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio", 70);

        // Ruta con menos paradas
        // pero más distancia
        grafo.agregarArista("uta", "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);

        // ═══════════════════════════════════
        // PRUEBAS
        // ═══════════════════════════════════

        System.out.println("===== BFS =====");

        List<String> rutaBFS = grafo.bfs("uta", "estadio");

        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA =====");

        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");

        grafo.mostrarRuta(rutaDijkstra);
    }
}