package com.randioo.randioo_server_base.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.randioo.randioo_server_base.utils.template.Function;

public class Observable {
	private Map<Observer, Function> observerMap = new ConcurrentHashMap<>();

	/**
	 * Adds an observer to the set of observers for this object, provided that
	 * it is not the same as some observer already in the set. The order in
	 * which notifications will be delivered to multiple observers is not
	 * specified. See the class comment.
	 *
	 * @param o an observer to be added.
	 * @throws NullPointerException if the parameter o is null.
	 */
	public void addObserver(Observer o, Function function) {

		if (o == null)
			throw new NullPointerException();

		if (!observerMap.containsKey(o)) {
			observerMap.put(o, function);
		}

	}

	/**
	 * Deletes an observer from the set of observers of this object. Passing
	 * <CODE>null</CODE> to this method will have no effect.
	 * 
	 * @param o the observer to be deleted.
	 */
	public void deleteObserver(Observer o) {
		observerMap.remove(o);
	}

	/**
	 * If this object has changed, as indicated by the <code>hasChanged</code>
	 * method, then notify all of its observers and then call the
	 * <code>clearChanged</code> method to indicate that this object has no
	 * longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two
	 * arguments: this observable object and <code>null</code>. In other words,
	 * this method is equivalent to: <blockquote><tt>
	 * notifyObservers(null)</tt></blockquote>
	 *
	 * @see java.util.Observable#clearChanged()
	 * @see java.util.Observable#hasChanged()
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void notifyObservers() {
		notifyObservers(null);
	}

	/**
	 * If this object has changed, as indicated by the <code>hasChanged</code>
	 * method, then notify all of its observers and then call the
	 * <code>clearChanged</code> method to indicate that this object has no
	 * longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two
	 * arguments: this observable object and the <code>arg</code> argument.
	 *
	 * @param arg any object.
	 * @see java.util.Observable#clearChanged()
	 * @see java.util.Observable#hasChanged()
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void notifyObservers(Object arg) {
		/*
		 * a temporary array buffer, used as a snapshot of the state of current
		 * Observers.
		 */

		/*
		 * We don't want the Observer doing callbacks into arbitrary code while
		 * holding its own Monitor. The code where we extract each Observable
		 * from the Vector and store the state of the Observer needs
		 * synchronization, but notifying observers does not (should not). The
		 * worst result of any potential race-condition here is that: 1) a
		 * newly-added Observer will miss a notification in progress 2) a
		 * recently unregistered Observer will be wrongly notified when it
		 * doesn't care
		 */

		for (Map.Entry<Observer, Function> entrySet : observerMap.entrySet()) {
			Function function = entrySet.getValue();
			try {
				function.apply(this, arg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public void deleteObservers() {
		observerMap.clear();
	}

	/**
	 * Returns the number of observers of this <tt>Observable</tt> object.
	 *
	 * @return the number of observers of this object.
	 */
	public int countObservers() {
		return observerMap.size();
	}
}
