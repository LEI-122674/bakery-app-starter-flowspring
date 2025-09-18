package com.vaadin.starter.bakery.backend.data.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link Product} entity.
 * <p>
 * This test class verifies the {@code equals} method implementation for the {@code Product} class,
 * ensuring that two Product instances are considered equal if their attributes match.
 */

public class ProductTest {

/**
 * Tests the {@code equals} method of the {@link Product} class.
 * <p>
 * Verifies that two Product instances with different names are not equal,
 * and that they become equal when their names and prices match.
 */
	
	@Test
	public void equalsTest() {
		Product o1 = new Product();
		o1.setName("name");
		o1.setPrice(123);

		Product o2 = new Product();
		o2.setName("anothername");
		o2.setPrice(123);

		Assert.assertNotEquals(o1, o2);

		o2.setName("name");
		Assert.assertEquals(o1, o2);
	}
}
