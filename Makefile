all: src/BandStructure.java
	javac -d bin/ -cp "lib/jfreechart-1.0.19/*:lib/jfreechart-1.0.19/lib/*" src/BandStructure.java
	jar -cvfm BandStructure.jar MANIFEST.MF -C bin .

clean:
	rm -r ./bin/*
	rm BandStructure.jar
