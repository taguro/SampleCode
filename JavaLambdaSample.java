import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


public class MainThread {

	public static void main(String[] args) {

		innerInterface test = ()->{System.out.println("hello");};
		test.express();

		//ラムダ式は、{}の外側で宣言している定数を参照できる
		String hoge ="{}の外側の定数";
		test = ()->{System.out.println(hoge);};
		test.express();

		//処理が1行の時は{}を省略できる
		String foo ="{}は省略OK";
		test = ()->System.out.println(foo);
		test.express();

		//もちろん引数を与えることもできる
		innerInterface2 test2 = (hoge2)->{System.out.println(hoge2);};
		test2.express("これは引数");

		//定義しないでも、引数を1つ受け取って、処理を行うためのインターフェースは用意されてる
		Consumer<String> test3 = (hoge2)->{System.out.println(hoge2);};
		test3.accept("返り値のない処理にはConsumer<T>を使おう");

		//引数を1つ受け取って値を返すためのインターフェース
		Function<String,Boolean> hogeCheck = (x)-> x == "hoge";
		if(hogeCheck.apply("hoge"))System.out.println("返り値のある処理にはFunction<T, R>を使おう");

		//引数を1つ受け取ってbooleanを返すインターフェースも既にある。他にも基本的なものは既にある。らしい。
		Predicate<String> hogeCheck2 = (x)-> x == "hoge";
		if(hogeCheck2.test("hoge")){System.out.println("booleanを返す処理にはPredicate<T>を使おう");}

		//上の例だとラムダ式（と無名クラス）のありがたさが分からないので、別のクラスに処理を埋め込む例
		String localVariable="この";
		innerClass3.showAfterCheck("別のクラスの処理に、このメソッドのローカル変数を組み込めた!", (x)->{return x.contains(localVariable);});

		//もちろん無名クラスを使って、こうすることもできる
		Predicate<String> pred= new Predicate<String>(){
			@Override
			public boolean test(String t) {
				return t.contains(localVariable);
			}
		};
		innerClass3.showAfterCheck("無名クラスを使ったこの方法もOK", pred);

		//無名クラスを使った方法を略記すると
		innerClass3.showAfterCheck("やっぱり無名クラスを使ったこの方法は少し書く文字が多い。new Predicate<String>ってわざわざ書きたくない", new Predicate<String>(){
			@Override
			public boolean test(String t) {
				return t.contains(localVariable);
			}
		});

		//実用上はこんな使い方。短いので少し嬉しい。
		String str = "Threadでラムダ式";
		Thread thread = new Thread(()->{System.out.println(str);});
		thread.start();



		//おまけ：引数2つ以上使いたいときどうするか？
		//こうするとFunction<String,String,Boolean> hoge = (x,y)->{return x.equals(y);};エラーになる
		//C#だと引数2つ以上の時も、既に定義されている型Action(返り値なしの処理の場合)とFunc(返り値のある処理の場合)で
		//こんな風にFunc<引数1の型,引数2の型,返り値の型> hoge = (x,y)=>{return x.equals(y);}　宣言できるけれど、javaではダメらしい。
		//どうするかというと、innerInterface4みたいなの用意したり、List使うとかか。あるいは引数で渡すのは諦めてローカル変数を使う構成にするとか。
		innerInterface4 inner4 = (x,y,z)->{System.out.println(x+y+z);return x+y+z;};
		inner4.express("引数を複数与えるために、@FunctionalInterface", "を", "使う方法");

		ArrayList array = new ArrayList();
		array.add(3);
		array.add("3");
		Predicate<ArrayList> test5 = (x)->{
			Object obj1 = x.get(0).toString();
			Object obj2 = (String)x.get(1);
			return obj1.equals(obj2);};
		if(test5.test(array)){System.out.println("ArrayListを使った方法も一応動く");}

	}

	//@FunctionalInterfaceというアノテーションを付けることで、ラムダ式のインターフェースを新たに定義できる
	@FunctionalInterface
	private interface innerInterface{
		public void express();
	}
	@FunctionalInterface
	private interface innerInterface2{
		public void express(String str);
	}

	@FunctionalInterface
	private interface innerInterface3{
		public void express(String str);
	}

	public static class innerClass3{
		public static void showAfterCheck(String str,Predicate<String> pred){
			if(pred.test(str))System.out.println(str);
		}
	}

	@FunctionalInterface
	private interface innerInterface4{
		public String express(String str,String str2, String str3);
	}

}
