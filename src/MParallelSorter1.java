import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MParallelSorter1 implements Sorter {

	private static final ExecutorService pool = Executors.newCachedThreadPool();
	
	private MSequentialSorter MSequentialSorter = new MSequentialSorter();

	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {

		List<Future<List<T>>> lists = new ArrayList<>();
		int split = 10;

		int L = list.size() / split;

		for (int i = 0; i < split; i++) {
			int fromIndex = i * L;
			int toIndex = i == split - 1 ? list.size() : (i + 1) * L;
			lists.add(pool.submit(()-> MSequentialSorter.sort(list.subList(fromIndex, toIndex))));
		}

		List<List<T>> res = new ArrayList<>();

		try {
			for (int i = 0; i < lists.size(); i++) {
				res.add(lists.get(i).get());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 

		while(res.size() > 1) {
			List<List<T>> temp = new ArrayList<>();
			for (int i = 0; i < res.size()/2; i++) {
				temp.add(MSequentialSorter.merge(res.get(i * 2), res.get(i * 2 + 1)));
			}
			if(!(res.size() % 2 == 0)) {
				temp.add(res.get(res.size() - 1));
			}
			res = temp;
		}

		return res.get(0);
	}
}