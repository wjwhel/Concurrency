import java.util.ArrayList;
import java.util.List;

public class MSequentialSorter implements Sorter {

  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
    if (list.size() <= 1) 
      return list;
    
    int mid = list.size() / 2;

    return merge(sort(list.subList(0, mid)) , sort(list.subList(mid, list.size())));
  }
  
  public <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right) {
    List<T> result = new ArrayList<>();
    int leftIndex = 0, rightIndex = 0, resultIndex = 0;
    
    while(leftIndex < left.size() && rightIndex < right.size()) {
      if(left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
        result.add(resultIndex++, left.get(leftIndex++));
      }
      else {
        result.add(resultIndex++, right.get(rightIndex++));
      }
    }
    
    if(leftIndex >= left.size()) { 
      result.addAll(right.subList(rightIndex, right.size()));
    }
    else {
      result.addAll(left.subList(leftIndex, left.size()));
    }
    
    return result;
  }
}