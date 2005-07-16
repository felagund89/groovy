class ExpandoPropertyTest extends GroovyTestCase {

    void testExpandoProperty() {
        def foo = new Expando()
        
        foo.cheese = "Cheddar"
        foo.name = "Gromit"
        
        assert foo.cheese == "Cheddar"
        assert foo.name == "Gromit"
        
        assert foo.expandoProperties.size() == 2
    }
    
    void testExpandoMethods() {
        def foo = new Expando()

        foo.cheese = "Cheddar"
        foo.fullName = "Gromit"
        foo.nameLength = { return this.fullName.length() }
        foo.multiParam = { a, b, c -> println("Called with ${a}, ${b}, ${c}"); return a + b + c }

        assert foo.cheese == "Cheddar"
        assert foo.fullName == "Gromit"
        assert foo.nameLength() == 6 , foo.nameLength()
        assert foo.multiParam(1, 2, 3) == 6
        
        // lets test using wrong number of parameters
        shouldFail { foo.multiParam(1) }
        shouldFail { foo.nameLength(1, 2) }
    }
    
    void testExpandoConstructorAndToString() {
        def foo = new Expando(type:"sometype", value:42)
        println foo
        assert foo.toString() == "{type=sometype, value=42}"
        assert "${foo}" == "{type=sometype, value=42}"
    }

    void testExpandoMethodOverrides() {
        def equals = { Object obj -> return obj.value == this.value }
        def foo = new Expando(type:"myfoo", value:42, equals:equals)
        def bar = new Expando(type:"mybar", value:43, equals:equals)
        def zap = new Expando(type:"myzap", value:42, equals:equals)
        println(foo)
	
        assert foo.equals(bar) == false
        assert foo.equals(zap) == true
	
        def list = []
        list << foo
        list << bar
        println list
	
        assert list.contains(foo) == true
        assert list.contains(bar) == true
        assert list.contains(zap) == true
        assert list.indexOf(bar) == 1
        assert list.indexOf(foo) == 0
	
        println "hashCode: " + foo.hashCode()
	
        foo.hashCode = { return this.value }
        println("hashCode: " + foo.hashCode())
	
        assert foo.hashCode() == foo.value
        println("toString: " + foo.toString())
	
        foo.toString = { return "Type: ${this.type}, Value: ${this.value}" }
        println("toString: " + foo.toString())
        assert foo.toString() == "Type: myfoo, Value: 42"
    }
}
