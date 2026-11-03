package com.sanad.model.donation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * DECORATOR PATTERN: every add-on a donor can choose, each made by its concrete decorator.
 * A new add-on is a new decorator class plus one line here.
 */
public enum AddOn {
    GIFT_WRAPPING("Gift wrapping", GiftWrappingDecorator.COST, GiftWrappingDecorator::new),
    THANK_YOU_CARD("Thank-you card", ThankYouCardDecorator.COST, ThankYouCardDecorator::new);

    private final String label;
    private final BigDecimal cost;
    private final UnaryOperator<DonationComponent> decorator;

    AddOn(String label, BigDecimal cost, UnaryOperator<DonationComponent> decorator) {
        this.label = label;
        this.cost = cost;
        this.decorator = decorator;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getCost() {
        return cost;
    }

    /** Wraps {@code component} in this add-on's decorator. */
    public DonationComponent wrap(DonationComponent component) {
        return decorator.apply(component);
    }

    static List<AddOn> parse(String codes) {
        if (codes == null || codes.isBlank()) return List.of();
        return Arrays.stream(codes.split(",")).map(AddOn::valueOf).toList();
    }

    static String codes(List<AddOn> addOns) {
        return addOns.stream().map(AddOn::name).collect(Collectors.joining(","));
    }
}
