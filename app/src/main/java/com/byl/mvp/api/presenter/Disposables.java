package com.byl.mvp.api.presenter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

/**
*@Title :   
*@Author : BaiYuliang 
*@Date :  
*@Desc : 
*/
public class Disposables implements Disposable {

    private Set<Disposable> disposables;
    private volatile boolean disposed;

    public Disposables() {

    }

    public Disposables(final Disposable... disposables) {
        this.disposables = new HashSet<>(Arrays.asList(disposables));
    }

    public void add(final Disposable s) {
        if (s.isDisposed()) {
            return;
        }
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    if (disposables == null) {
                        disposables = new HashSet<>();
                    }
                    disposables.add(s);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        s.dispose();
    }

    public void remove(final Disposable s) {
        if (!disposed) {
            boolean unsubscribe = false;
            synchronized (this) {
                if (disposed || disposables == null) {
                    return;
                }
                unsubscribe = disposables.remove(s);
            }
            if (unsubscribe) {
                // if we removed successfully we then need to call unsubscribe on it (outside of the lock)
                s.dispose();
            }
        }
    }

    /**
     * Unsubscribes any disposables that are currently part of this {@code CompositeDisposable} and remove
     * them from the {@code CompositeDisposable} so that the {@code CompositeDisposable} is empty and
     * able to manage new disposables.
     */
    public void clear() {
        if (!disposed) {
            Collection<Disposable> unsubscribe = null;
            synchronized (this) {
                if (disposed || disposables == null) {
                    return;
                } else {
                    unsubscribe = disposables;
                    disposables = null;
                }
            }
            disposeFromAll(unsubscribe);
        }
    }

    private static void disposeFromAll(Collection<Disposable> disposables) {
        if (disposables == null) {
            return;
        }
        for (Disposable s : disposables) {
            try {
                s.dispose();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
            }
        }

    }

    /**
     * Returns true if this composite is not disposed and contains disposables.
     *
     * @return {@code true} if this composite is not disposed and contains disposables.
     * @since 1.0.7
     */
    public boolean hasDisposables() {
        if (!disposed) {
            synchronized (this) {
                return !disposed && disposables != null && !disposables.isEmpty();
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        if (!disposed) {
            Collection<Disposable> dispose;
            synchronized (this) {
                if (disposed) {
                    return;
                }
                disposed = true;
                dispose = disposables;
                disposables = null;
            }
            // we will only get here once
            disposeFromAll(dispose);
        }
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

}
