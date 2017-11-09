package org.tensorflow.demo.values;

/**
* Provides an interface for classes that can be represented by a Value.
*/
public interface Evaluateable {
    /**
    * @Return true if a valid Value can be returned by calling evaluate.
    */
    boolean isEvaluatable();
    
    /**
    * @Return a value corresponding to the status of this element.
    */
    Value evaluate();
}
