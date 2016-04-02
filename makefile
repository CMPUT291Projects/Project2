all: main

main:
	javac *.java

run: main
	CLASSPATH=$CLASSPATH:.:/usr/share/java/db.jar:.
	export CLASSPATH
	LD_LIBRARY_PATH=/oracle/lib
	export LD_LIBRARY_PATH
	java Main

clean: 
	rm *.class *~
