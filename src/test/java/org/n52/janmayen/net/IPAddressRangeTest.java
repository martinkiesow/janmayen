/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.janmayen.net;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @since 1.0.0
 *
 */
public class IPAddressRangeTest {
    @Test
    public void should_return_true_for_ip_addresses_in_cidr_range() {
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.55.105"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.4.240"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.1.1"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.2.16"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.255.255"), is(true));
    }

    @Test
    public void should_return_true_for_ip_addresses_in_subnet_range() {
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.55.105"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.4.240"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.1.1"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.2.16"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.255.255"), is(true));
    }

    @Test
    public void should_return_false_for_ip_addresses_outside_of_cidr_range() {
        assertThat(isAddressInRange("192.168.0.0/16", "10.1.16.100"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "1.1.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "255.255.255.255"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "192.167.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "192.169.1.1"), is(false));
    }

    @Test
    public void should_return_false_for_ip_addresses_outside_of_subnet_range() {
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "10.1.16.100"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "1.1.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "255.255.255.255"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.167.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.169.1.1"), is(false));
    }

    @Test
    public void should_return_true_for_single_ip_cidr_range() {
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.9"), is(true));
    }
    @Test
    public void should_return_true_for_single_ip_subnet_range() {
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.9"), is(true));
    }

    @Test
    public void should_return_false_for_ips_outside_of_single_ip_cidr_range() {
        assertThat(isAddressInRange("192.168.15.9/32", "0.0.0.0"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.8"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.10"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.14.9"), is(false));
    }

    @Test
    public void should_return_false_for_ips_outside_of_single_ip_subnet_range() {
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "0.0.0.0"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.8"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.10"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.14.9"), is(false));
    }

    @Test
    public void should_return_true_for_valid_ip() {
        assertThat(isValidAddress("192.168.1.1"), is(true));
        assertThat(isValidAddress("127.0.0.1"), is(true));
    }

    @Test
    public void should_return_false_for_invalid_ip() {
        assertThat(isValidAddress("192.168.1.256"), is(false));
        assertThat(isValidAddress("192.168.1.1.9"), is(false));
        assertThat(isValidAddress("192.168.1"), is(false));
        assertThat(isValidAddress("192.168.01"), is(false));
        assertThat(isValidAddress("i am not an ip"), is(false));
    }

    @Test
    public void should_return_true_for_valid_cidr_addresses() {
        assertThat(isValidAddressRange("192.168.1.1/32"), is(true));
        assertThat(isValidAddressRange("127.0.0.1/32"), is(true));
        assertThat(isValidAddressRange("192.168.1.1/10"), is(true));
        assertThat(isValidAddressRange("1.1.1.1/0"), is(true));
    }

    @Test
    public void should_return_true_for_valid_subnet_addresses() {
        assertThat(isValidAddressRange("192.168.1.1/255.255.255.0"), is(true));
        assertThat(isValidAddressRange("127.0.0.1/255.255.0.0"), is(true));
        assertThat(isValidAddressRange("192.168.1.1/255.0.0.0"), is(true));
        assertThat(isValidAddressRange("1.1.1.1/0.0.0.0"), is(true));
    }

    @Test
    public void should_return_false_for_invalid_subnet_addresses() {
        assertThat(isValidAddressRange("192.168.1.256/255.255"), is(false));
        assertThat(isValidAddressRange("192.168.1.1.9/255.255.255.0.1"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/255.a.255.0"), is(false));
    }

    @Test
    public void should_return_false_for_invalid_cidr_addresses() {
        assertThat(isValidAddressRange("192.168.1.256/32"), is(false));
        assertThat(isValidAddressRange("192.168.1.1.9/32"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/-1"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/33"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/a"), is(false));
        assertThat(isValidAddressRange("192.168.1.01/a"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/"), is(false));
        assertThat(isValidAddressRange("192.168.1.a/32"), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_illegal_argument_exception() {
        isAddressInRange("192.168.0.0/a", "192.168.55.105");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_illegal_argument_exception2() {
        isAddressInRange("192.168.0.0/16", "192.168.55.a");
    }

    private boolean isAddressInRange(String range, String address) {
        return new IPAddressRange(range).contains(new IPAddress(address));
    }

    private boolean isValidAddress(String address) {
        try {
            new IPAddress(address);
        } catch(IllegalArgumentException e) {
            return false;
        }
        return true;

    }

    private boolean isValidAddressRange(String address) {
        try {
            new IPAddressRange(address);
        } catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
