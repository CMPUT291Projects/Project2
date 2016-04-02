all: main

main:
	javac *.java

run: main
	java Main

clean: 
	rm *.class *~
