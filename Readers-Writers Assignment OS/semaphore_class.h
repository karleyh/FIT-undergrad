#include <unistd.h>     /* Symbolic Constants */
#include <sys/types.h>  /* Primitive System Data Types */
#include <errno.h>      /* Errors */
#include <stdio.h>      /* Input/Output */
#include <stdlib.h>     /* General Utilities */
#include <pthread.h>    /* POSIX Threads */
#include <string.h>     /* String handling */
#include <semaphore.h>  /* Semaphore */
#include <iostream>
using namespace std;

/*
Semaphore class with a lock/unlock switch
Karley Herschelman

 This wrapper class for semaphore.h functions is from:
 http://stackoverflow.com/questions/2899604/using-sem-t-in-a-qt-project
 */
class Semaphore {
public:
    // Constructor
    Semaphore(int initialValue)
    {
        sem_init(&mSemaphore, 0, initialValue);
    }
    // Destructor
    ~Semaphore()
    {
        sem_destroy(&mSemaphore); /* destroy semaphore */
    }
    
    // wait
    void wait()
    {
        sem_wait(&mSemaphore);
    }
    // signal
    void signal()
    {
        sem_post(&mSemaphore);
    }
	void lock(Semaphore s)
	{
        wait();
		counter += 1;
		if (counter == 1)
		{
			s.wait();
		}
		signal();
	}  
    void unlock(Semaphore s)
	{
        wait();
		counter += 1;
		if (counter == 0)
		{
			s.signal();
		}
		signal();
	}
	
private:
    sem_t mSemaphore;
	int counter;
};