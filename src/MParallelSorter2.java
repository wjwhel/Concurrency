import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MParallelSorter2 implements Sorter {
	
	private MSequentialSorter MSequentialSorter = new MSequentialSorter();

	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {

		List<CompletableFuture<List<T>>> lists = new ArrayList<>();
		int split = 10;

		int L = list.size() / split;

		for (int i = 0; i < split; i++) {
			int fromIndex = i * L;
			int toIndex = i == split - 1 ? list.size() : (i + 1) * L;
			lists.add(CompletableFuture.supplyAsync(()->MSequentialSorter.sort(list.subList(fromIndex, toIndex))));
		}

		while(lists.size() > 1) {
			List<CompletableFuture<List<T>>> temp = new ArrayList<>();
			for (int i = 0; i < lists.size()/2; i++) {
				temp.add(lists.get(i * 2).thenCombine(lists.get(i * 2 + 1), MSequentialSorter::merge));
			}
			if(!(lists.size() % 2 == 0)) {
				temp.add(lists.get(lists.size() - 1));
			}
			lists = temp;
		}

		return lists.get(0).join();
	}
}