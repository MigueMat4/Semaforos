package Clases;

/**
 * Este programa implementa la aplicación de un monitor para la solución del
 * problema del productor-consumidor.
 */

/**
 *
 * @author Migue
 * @version 1.0
 * @since   2019-03-26
 */
public class Monitor {
    static final int N = 5; // constante que proporciona el tamaño del búfer
    static productor p = new productor(); // crea instancia de un nuevo hilo productor
    static consumidor c = new consumidor(); // crea instancia de un nuevo hilo consumidor
    static super_monitor monitor = new super_monitor(); // crea instancia de un nuevo monitor
    
    public static void main(String args[]){
        p.start(); // inicia el hilo productor
        c.start(); // inicia el hilo consumidor
    }
    
    static class productor extends Thread {
        public void run(){
            int elemento;
            while(true){ // ciclo del productor
                elemento = producir_elemento();
                monitor.insertar(elemento);
            }
        }
        
        private int producir_elemento(){
            return (int)(Math.random() * 9) + 1;
        }
    }
    
    static class consumidor extends Thread{
        public void run() {
            int elemento;
            while(true){ // ciclo del consumidor
                elemento = monitor.eliminar();
                consumir_elemento(elemento);
            }
        }
        
        private void consumir_elemento(int elemento){
            System.out.println(elemento + " consumido");
        }
    }
    
    static class super_monitor{ // monitor como solución
        private int bufer[] = new int[N];
        private int cuenta=0, inf=0, sup=0; // contadores e índices
        
        /**
         * synchronized = Una vez un hilo ha empezado a ejecutar ese método, 
         * no se permitirá que ningún otro hilo empiece a ejecutar ningún otro 
         * método synchronized de ese objeto
         */
        public synchronized void insertar(int val){
            if (cuenta==N)
                ir_a_estado_inactivo(); // si el búfer está lleno, pasa al estado inactivo
            bufer[sup]=val; // inserta un elemento en el búfer
            sup=(sup+1)%N; // ranura en la que se va a colocar el siguiente elemento
            cuenta=cuenta+1; // ahora hay un elemento más en el búfer
            System.out.println(val + " ingresado");
            if (cuenta==1)
                notify(); // si el consumidor estaba inactivo, lo despierta [signal]
        }
        
        public synchronized int eliminar(){
            int val;
            if (cuenta==0)
                ir_a_estado_inactivo(); // si el búfer está vacío, pasa al estado inactivo
            val=bufer[inf]; // obtiene un elemento del búfer
            inf = (inf + 1) %N; // ranura en la que se va a colocar el siguiente elemento
            cuenta=cuenta-1; // un elemento menos en el búfer
            if (cuenta==N-1)
                notify(); // si el productor estaba inactivo, lo despierta [signal]
            return val;
        }
        
        private void ir_a_estado_inactivo(){
            try{
                wait(); // Duerme al proceso en turno
            }
            catch(InterruptedException exc){};
        }
    }
}
