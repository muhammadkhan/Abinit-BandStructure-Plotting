all: src/BandStructure.java
	javac -d bin/ -cp "lib/jfreechart-1.0.19/*:lib/jfreechart-1.0.19/lib/*" src/*.java
	jar -cvfm BandStructure.jar MANIFEST.MF -C bin .

clean:
	rm -r ./bin/*
	rm BandStructure.jar
