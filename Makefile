all: src/BandStructure.java
	mkdir -p bin
	javac -d bin/ -cp "lib/*" src/*.java
	jar -cvfm BandStructure.jar MANIFEST.MF -C bin .

clean:
	rm -r ./bin/*
	rm -r ./bin
	rm BandStructure.jar
