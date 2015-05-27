/*
 * client.c
 *
 *  Created on: Mar 3, 2015
 *      Author: Karley Herschelman
 * 	sends a value to the server and prints the returned result
 */

#include<stdio.h>
#include<string.h>
#include<sys/socket.h>
#include<arpa/inet.h>

int main(int argc , char *argv[])
{
	int sock_id;
	struct sockaddr_in server;
	int value , server_reply;

	//Create socket
	sock_id = socket(AF_INET , SOCK_STREAM , 0);
	if (sock_id < 0)
	{
		perror("Could not create socket");
	}
	else
	{
		printf("Socket created");
	}

	server.sin_addr.s_addr = inet_addr("127.0.0.1");
	server.sin_family = AF_INET;
	server.sin_port = htons( 5575 );

	//Connect to remote server
	if (connect(sock_id , (struct sockaddr *)&server , sizeof(server)) < 0)
	{
		perror("connection to server failed");
		return 1;
	}

	puts("Connected to server\n");

	//keep communicating with server until the reply from the server fails
	while(1)
	{
		printf("Enter number : ");
		scanf("%d" , &value);

		//Send some data
		if( send(sock_id , &value , sizeof(value) , 0) < 0)
		{
			puts("Send failed");
			return 1;
		}

		//Receive a reply from the server
		if( recv(sock_id , &server_reply , sizeof(server_reply) , 0) < 0)
		{
			perror("reply failed");
			break;
		}

		printf("Factorial of number : %d\n",  server_reply);
	}

	close(sock_id);
	return 0;
}

