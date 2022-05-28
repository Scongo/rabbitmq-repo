package co.za.paygate.rabbit.config;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class Op<T> {

    private static final Op<?> EMPTY = new Op<>();

    private final T value;


    public Op(T value) {
        this.value = value;
    }

    private Op() {
        this.value = null;
    }

    public static <T> Op<T> of(T value) {
        return new Op<T>(value);
    }

    public T get() {
        return this.value;
    }

    public <P> P getAs(Class<P> clazz) {
        return clazz.cast(this.value);
    }

    public T get(T val) {
        return value == null ? val : value;
    }

    public T empty(T val) {
        return value == null || value.equals("") ? val : value;
    }

    public Integer asInt(Integer val) {
        return value == null || value.equals("") ? val : Integer.parseInt(value.toString());
    }

    public Integer asInt() {
        return value == null || value.equals("") ? null : Integer.parseInt(value.toString());
    }

    public String asString() {
        return value == null ? null : String.valueOf(value);
    }

    public String asString(String val) {
        return value == null ? val : String.valueOf(value);
    }

    public static <T> Op<T> empty() {
        @SuppressWarnings("unchecked")
        Op<T> t = (Op<T>) EMPTY;
        return t;
    }

    public <U> Op<U> flatMap(Function<? super T, Op<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    public <U> Op<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {

            return Op.ofNullable(mapper.apply(value));
        }
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    public static <T> Op<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isPresentAndNotEmpty() {
        return value != null && !"".equals(value);
    }

    public Op<T> isPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);

        return this;
    }

    public void isNotPresent(Consumer<? super T> consumer) {
        if (value == null)
            consumer.accept(value);
    }

    public Op<T> isNotPresentThrow(Exception e) throws Exception {
        if (value == null) {
            throw e;
        } else {
            return this;
        }
    }

    public Op<T> isPresentThrow(Exception e) throws Exception {
        if (value != null) {
            throw e;
        } else {
            return this;
        }
    }
}
