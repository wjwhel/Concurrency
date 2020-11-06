import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MParallelSorter3 implements Sorter {

	public static final ForkJoinPool mainPool = new ForkJoinPool();
	
	private MSequentialSorter MSequentialSorter = new MSequentialSorter();

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		@SuppressWarnings("serial")
		class Sort extends RecursiveTask<List<T>>{
			List<T> list;
			
			Sort(List<T> list){
				this.list = list;
			}

			@Override
			protected List<T> compute() {
				if (list.size() <= 20) return MSequentialSorter.sort(list);

				int mid = list.size() / 2;
				
				Sort left = new Sort(list.subList(0, mid));
				Sort right = new Sort(list.subList(mid, list.size()));
				invokeAll(left, right);

				return MSequentialSorter.merge(left.join(), right.join());
			}
		}
		
		return mainPool.invoke(new Sort(list));
	}
}