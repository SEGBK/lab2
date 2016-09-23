# Makefile - lab2
# Contains all build information to make life easier.

GAME = Game
SRC  = lib/*.java $(GAME).java test/*.java
DOCS = docs
TEST = test/Runner

all:
	javac $(GAME).java

run: all
	java $(GAME)

$(DOCS): .PHONY
	javadoc $(SRC) -d $(DOCS)

test: .PHONY
	javac $(TEST).java
	java $(TEST)

clean:
	rm -f *.class lib/*.class test/*.class
	rm -rf $(DOCS)

.PHONY:

