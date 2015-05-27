//No Starve Readers-Writers Solution
//Karley Herschelman
//
// example from: http://www.amparo.net/ce155/sem-ex.c
//
// adapted using some code from Downey's book on semaphores
//
// compilation:
//
//       g++ no_starve.cpp -lpthread -o no_starve -lm
//
//
//
/* Includes */

#include "semaphore_class.h"

/* prototype for thread routine */
void *reader ( void *ptr );
void *writer ( void *ptr );


/* global vars */
/* semaphores are declared global so they can be accessed
 in main() and in thread routine. */
//Semaphore B_Done(0);

//int readers = 0;
Semaphore roomEmpty(1); 
Semaphore lightswitch (1); 
Semaphore turnstile(1);

int main()
{	
    int i[3];
    pthread_t thread_a;
    pthread_t thread_b;
    i[0] = 0; /* argument to threads */
    i[1] = 1;
    
    
    /* Note: you can check if thread has been
     successfully created by checking return value of
     pthread_create */
    pthread_create (&thread_a, NULL, reader, (void *) &i[0]);
    pthread_create (&thread_b, NULL, writer, (void *) &i[1]);
    
    // Join threads
    (void) pthread_join(thread_a, NULL);
    (void) pthread_join(thread_b, NULL);
    
    /* exit */
    exit(0);
    
} /* main() */

void *reader (void *ptr)
{
    int x;
    x = *((int *) ptr);
	turnstile.wait();
	turnstile.signal();
	lightswitch.lock (roomEmpty);
	printf ("Reading can now occur \n");
	lightswitch.unlock(roomEmpty);	
	pthread_exit(0); /* exit thread */
}

void *writer (void *ptr)
{
    int x;
    x = *((int *) ptr);
	turnstile.wait();
	roomEmpty.wait();
	printf ("Writing can now occur \n");
	turnstile.signal();
	roomEmpty.signal();
	pthread_exit(0); /* exit thread */
}
