# Makefile - lab2
# Contains all build information to make life easier.

GAME = Game
SRC  = lib/*.java $(GAME).java
DOCS = docs
TEST = test/Runner

all:
	javac $(SRC)

run: all
	java $(GAME)

$(DOCS): .PHONY
	javadoc $(SRC) -d $(DOCS)

test: .PHONY
	javac test/*.java
	java $(TEST)

clean:
	rm -f *.class lib/*.class test/*.class

.PHONY: