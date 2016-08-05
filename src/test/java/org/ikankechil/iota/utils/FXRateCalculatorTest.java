/**
 * FXRateCalculatorTest.java  v0.3  14 April 2015 11:02:35 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.utils;

import static org.ikankechil.iota.utils.FXRateCalculator.Operator.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.io.OHLCVReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test for <code>FXRateCalculator</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class FXRateCalculatorTest {

  private final FXRateCalculator                    fxc             = new FXRateCalculator(INPUT_DIRECTORY);

  @Rule
  public final ExpectedException                    thrown          = ExpectedException.none();

  private static final Map<String, OHLCVTimeSeries> EXPECTEDS       = new HashMap<>();
  private static final File                         INPUT_DIRECTORY = new File(".//./src/test/resources/" + FXRateCalculatorTest.class.getSimpleName());
  private static final File                         DERIVED         = INPUT_DIRECTORY.listFiles(new FileFilter() {
                                                                        @Override
                                                                        public boolean accept(final File pathname) {
                                                                          return pathname.isDirectory();
                                                                        }
                                                                      })[0];

  // Currencies (ISO 4217)
  private static final String                       USD             = "USD";
  private static final String                       EUR             = "EUR";
  private static final String                       SGD             = "SGD";
  private static final String                       THB             = "THB";
  private static final String                       CHF             = "CHF";
  private static final String                       GBP             = "GBP";
  private static final String                       NZD             = "NZD";
  private static final String                       AUD             = "AUD";
  private static final String                       KRW             = "KRW";
  private static final String                       JPY             = "JPY";
  private static final String                       ZAR             = "ZAR";

  // Currency pairs
  private static final String                       SGDTHB          = SGD + THB;
  private static final String                       THBSGD          = THB + SGD;
  private static final String                       SGDKRW          = SGD + KRW;
  private static final String                       KRWSGD          = KRW + SGD;
  private static final String                       USDGBP          = USD + GBP;
  private static final String                       GBPUSD          = GBP + USD;
  private static final String                       USDAUD          = USD + AUD;
  private static final String                       AUDUSD          = AUD + USD;

  private static final String                       CHFGBP          = CHF + GBP;
  private static final String                       GBPSGD          = GBP + SGD;
  private static final String                       NZDAUD          = NZD + AUD;

  // Constants
  private static final double                       DELTA           = 0.00000005; // 0.0005 pips, empirically determined
  private static final String                       EMPTY           = "";
  private static final String                       CSV             = ".csv";

  private static final String[]                     INAPPROPRIATES  = { "AB", "ABCD", "GBP_I",
                                                                        "!@", "#$%", "*()_+",
                                                                        "12", "345", "67890" };

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    final OHLCVReader reader = new OHLCVReader();
    for (final String pair : Arrays.asList(SGDTHB, CHFGBP, GBPSGD, NZDAUD, SGDKRW, THBSGD, KRWSGD, USDGBP, USDAUD)) {
      EXPECTEDS.put(pair, reader.read(new File(DERIVED, pair + CSV)));
    }
    for (final String pair : Arrays.asList(GBPUSD, AUDUSD)) {
      EXPECTEDS.put(pair, reader.read(new File(INPUT_DIRECTORY, pair + CSV)));
    }
  }

  @AfterClass
  public static void tearDownAfterClass() {
    EXPECTEDS.clear();
  }

  @SuppressWarnings("unused")
  @Test
  public void cannotInstantiateWithNullDirectory() {
    thrown.expect(NullPointerException.class);
    new FXRateCalculator(null);
  }

  @SuppressWarnings("unused")
  @Test
  public void cannotInstantiateWithNullDirectory2() {
    thrown.expect(NullPointerException.class);
    new FXRateCalculator(null, USD);
  }

  @SuppressWarnings("unused")
  @Test
  public void cannotInstantiateWithFile() {
    thrown.expect(IllegalArgumentException.class);
    new FXRateCalculator(new File(EMPTY));
  }

  @SuppressWarnings("unused")
  @Test
  public void cannotInstantiateWithFile2() {
    thrown.expect(IllegalArgumentException.class);
    new FXRateCalculator(new File(EMPTY), USD);
  }

  @Test
  public void instantiateWithInappropriatePreferredCommonCurrency() {
    assertNotNull(new FXRateCalculator(INPUT_DIRECTORY, null));
    assertNotNull(new FXRateCalculator(INPUT_DIRECTORY, EMPTY));
    for (final String inappropriate : INAPPROPRIATES) {
      assertNotNull(new FXRateCalculator(INPUT_DIRECTORY, inappropriate));
    }
  }

  @Test
  public void cannotComputeFXRatesWithInappropriateBaseCurrency() throws IOException {
    for (final String inappropriate : INAPPROPRIATES) {
      try {
        fxc.computeFXRates(USD, inappropriate);
        fail("Inappropriate quote currency: " + inappropriate);
      }
      catch (final IllegalArgumentException iaE) {
        assertNotNull(iaE.getMessage());
      }
    }
  }

  @Test
  public void cannotComputeFXRatesWithInappropriateQuoteCurrency() throws IOException {
    for (final String inappropriate : INAPPROPRIATES) {
      try {
        fxc.computeFXRates(inappropriate, USD);
        fail("Inappropriate base currency: " + inappropriate);
      }
      catch (final IllegalArgumentException iaE) {
        assertNotNull(iaE.getMessage());
      }
    }
  }

  @Test
  public void cannotComputeFXRatesWithNullBaseCurrency() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates((String) null, USD);
  }

  @Test
  public void cannotComputeFXRatesWithNullBaseCurrency2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates((String) null, Collections.singletonList(USD));
  }

  @Test
  public void cannotComputeFXRatesWithNullBaseCurrency3() throws InterruptedException{
    thrown.expect(NullPointerException.class);
    fxc.computeFXRates((Collection<String>) null, USD);
  }

  @Test
  public void cannotComputeFXRatesWithNullBaseCurrency4() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.<String>singletonList(null), USD);
  }

  @Test
  public void cannotComputeFXRatesWithNullQuoteCurrency() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, (String) null);
  }

  @Test
  public void cannotComputeFXRatesWithNullQuoteCurrency2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.singletonList(USD), (String) null);
  }

  @Test
  public void cannotComputeFXRatesWithNullQuoteCurrency3() throws InterruptedException{
    thrown.expect(NullPointerException.class);
    fxc.computeFXRates(USD, (Collection<String>) null);
  }

  @Test
  public void cannotComputeFXRatesWithNullQuoteCurrency4() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, Collections.<String>singletonList(null));
  }

  @Test
  public void cannotComputeFXRatesWithEmptyBaseCurrency() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(EMPTY, USD);
  }

  @Test
  public void cannotComputeFXRatesWithEmptyBaseCurrency2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(EMPTY, Collections.singletonList(USD));
  }

  @Test
  public void cannotComputeFXRatesWithEmptyBaseCurrency3() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.<String>emptyList(), USD);
  }

  @Test
  public void cannotComputeFXRatesWithEmptyBaseCurrency4() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.singletonList(EMPTY), USD);
  }

  @Test
  public void cannotComputeFXRatesWithEmptyQuoteCurrency() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, EMPTY);
  }

  @Test
  public void cannotComputeFXRatesWithEmptyQuoteCurrency2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.singletonList(USD), EMPTY);
  }

  @Test
  public void cannotComputeFXRatesWithEmptyQuoteCurrency3() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, Collections.<String>emptyList());
  }

  @Test
  public void cannotComputeFXRatesWithEmptyQuoteCurrency4() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, Collections.singletonList(EMPTY));
  }

  @Test
  public void cannotComputeFXRatesWithIdenticalBaseAndQuoteCurrencies() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, USD);
  }

  @Test
  public void cannotComputeFXRatesWithIdenticalBaseAndQuoteCurrencies2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(USD, Collections.singletonList(USD));
  }

  @Test
  public void cannotComputeFXRatesWithIdenticalBaseAndQuoteCurrencies3() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.computeFXRates(Collections.singletonList(USD), USD);
  }

  @Test
  public void cannotComputeFXRatesWithoutBaseCurrencySource() throws IOException {
    final OHLCVTimeSeries actual = fxc.computeFXRates(ZAR, SGD);
    assertEquals(0, actual.size());
    assertEquals(ZAR + SGD, actual.toString());
  }

  @Test
  public void cannotComputeFXRatesWithoutQuoteCurrencySource() throws IOException {
    final OHLCVTimeSeries actual = fxc.computeFXRates(SGD, ZAR);  // USDSGD exists
    assertEquals(0, actual.size());
    assertEquals(SGD + ZAR, actual.toString());
  }

  @Test
  public void cannotComputeFXRatesWithoutQuoteCurrencySource2() throws IOException {
    final OHLCVTimeSeries actual = fxc.computeFXRates(GBP, ZAR);  // GBPUSD exists
    assertEquals(0, actual.size());
    assertEquals(GBP + ZAR, actual.toString());
  }

  @Test
  public void cannotComputeExistingCommonMajorBase() throws IOException {
    final OHLCVTimeSeries actual = fxc.computeFXRates(USD, SGD);
    assertEquals(0, actual.size());
  }

  @Test
  public void cannotComputeExistingCommonMajorBase2() throws InterruptedException{
    final List<OHLCVTimeSeries> actuals = fxc.computeFXRates(USD, Collections.singletonList(SGD));
    assertEquals(0, actuals.get(0).size());
  }

  @Test
  public void cannotComputeExistingCommonMajorBase3() throws InterruptedException{
    final List<OHLCVTimeSeries> actuals = fxc.computeFXRates(Collections.singletonList(USD), SGD);
    assertEquals(0, actuals.get(0).size());
  }

  @Test
  public void computeFXRatesCommonBase() throws IOException {
    // 1. common is "base" of both base and quote -> computation: quote / base
    //    e.g. SGDTHB = USDTHB / USDSGD
    final OHLCVTimeSeries actual = fxc.computeFXRates(SGD, THB);
    compare(EXPECTEDS.get(SGDTHB), actual);
  }

  @Test
  public void computeFXRatesDifferentBase1() throws IOException {
    // 2. common is "base" of base and "quote" of quote -> computation: 1 / (base * quote)
    //    e.g. CHFGBP = 1 / (USDCHF * GBPUSD)
    final OHLCVTimeSeries actual = fxc.computeFXRates(CHF, GBP);
    compare(EXPECTEDS.get(CHFGBP), actual);
  }

  @Test
  public void computeFXRatesDifferentBase2() throws IOException {
    // 3. common is "quote" of base and "base" of quote -> computation: base * quote
    //    e.g. GBPSGD = GBPUSD * USDSGD
    final OHLCVTimeSeries actual = fxc.computeFXRates(GBP, SGD);
    compare(EXPECTEDS.get(GBPSGD), actual);
  }

  @Test
  public void computeFXRatesCommonQuote() throws IOException {
    // 4. common is "quote" of both base and quote -> computation: base / quote
    //    e.g. NZDAUD = NZDUSD / AUDUSD
    final OHLCVTimeSeries actual = fxc.computeFXRates(NZD, AUD);
    compare(EXPECTEDS.get(NZDAUD), actual);
  }

  @Test
  public void recomputeFXRates() throws IOException {
    final OHLCVTimeSeries expected = fxc.computeFXRates(USD, THB);
    final OHLCVTimeSeries actual = fxc.computeFXRates(USD, THB);
    assertNotSame(expected, actual);
    compare(expected, actual);
  }

  @Ignore@Test
  public void speed() throws InterruptedException{
    final List<OHLCVTimeSeries> fxRates = fxc.computeFXRates(Arrays.asList(THB, KRW, CHF, AUD, GBP, NZD), SGD);
    final long start = System.currentTimeMillis();
    for (int i = 0; i < 1; ++i) {
      fxc.invertFXRates(fxRates);
    }
    System.out.println(System.currentTimeMillis() - start);
  }

  @Test
  public void removeBaseCurrencyWhenInQuoteCurrencies() throws InterruptedException{
    final List<String> quoteCurrencies = new ArrayList<>();
    Collections.addAll(quoteCurrencies, THB, KRW, SGD);
    final List<OHLCVTimeSeries> actuals = fxc.computeFXRates(SGD, quoteCurrencies);

    int i = -1;
    quoteCurrencies.remove(SGD);

    for (final String quoteCurrency : quoteCurrencies) {
      final OHLCVTimeSeries expected = EXPECTEDS.get(SGD + quoteCurrency);
      compare(expected, actuals.get(++i));
    }

    assertEquals(quoteCurrencies.size(), actuals.size());
  }

  @Test
  public void removeQuoteCurrencyWhenInBaseCurrencies() throws InterruptedException{
    final List<String> baseCurrencies = new ArrayList<>();
    Collections.addAll(baseCurrencies, THB, KRW, SGD);
    final List<OHLCVTimeSeries> actuals = fxc.computeFXRates(baseCurrencies, SGD);

    int i = -1;
    baseCurrencies.remove(SGD);

    for (final String baseCurrency : baseCurrencies) {
      final OHLCVTimeSeries expected = EXPECTEDS.get(baseCurrency + SGD);
      compare(expected, actuals.get(++i));
    }

    assertEquals(baseCurrencies.size(), actuals.size());
  }

  @Test
  public void usdIsFirstPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcUSD = new FXRateCalculator(INPUT_DIRECTORY, USD);
    compare(fxcUSD.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void eurIsAnotherPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcEUR = new FXRateCalculator(INPUT_DIRECTORY, EUR);
    // no files with common currency EUR
    compare(fxcEUR.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void chfIsAnotherPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcCHF = new FXRateCalculator(INPUT_DIRECTORY, CHF);
    // no files with common currency CHF
    compare(fxcCHF.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void gbpIsAnotherPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcGBP = new FXRateCalculator(INPUT_DIRECTORY, GBP);
    // no files with common currency GBP
    compare(fxcGBP.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void jpyIsAnotherPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcJPY = new FXRateCalculator(INPUT_DIRECTORY, JPY);
    // no files with common currency JPY
    compare(fxcJPY.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void computeFXRatesWithPreferredCommonCurrency() throws IOException {
    final FXRateCalculator fxcZAR = new FXRateCalculator(INPUT_DIRECTORY, ZAR);
    // no files with common currency ZAR
    compare(fxcZAR.computeFXRates(SGD, THB), fxc.computeFXRates(SGD, THB));
  }

  @Test
  public void lowerCaseCurrenciesConvertedToUpperCase() throws IOException {
    final OHLCVTimeSeries actual = fxc.computeFXRates(SGD.toLowerCase(), THB.toLowerCase());
    assertEquals(SGDTHB, actual.toString());
    compare(EXPECTEDS.get(SGDTHB), actual);
  }

  @Test
  public void cannotInvertNullFXRates() throws IOException {
    thrown.expect(NullPointerException.class);
    fxc.invertFXRates((OHLCVTimeSeries) null);
  }

  @Test
  public void cannotInvertNullFXRates2() throws InterruptedException{
    thrown.expect(NullPointerException.class);
    fxc.invertFXRates((Collection<OHLCVTimeSeries>) null);
  }

  @Test
  public void cannotInvertNullFXRates3() throws InterruptedException{
//    thrown.expect(NullPointerException.class);
//    fxc.invertFXRates(Collections.<OHLCVTimeSeries>singletonList(null));
    final List<OHLCVTimeSeries> actuals = fxc.invertFXRates(Collections.<OHLCVTimeSeries>singletonList(null));
    assertEquals(0, actuals.get(0).size());
  }

  @Test
  public void cannotInvertEmptyFXRates() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    fxc.invertFXRates(new OHLCVTimeSeries(SGDTHB, 0));
  }

  @Test
  public void cannotInvertEmptyFXRates2() throws InterruptedException{
    thrown.expect(IllegalArgumentException.class);
    fxc.invertFXRates(Collections.<OHLCVTimeSeries>emptyList());
  }

  @Test
  public void cannotInvertFXRatesWithBadName() throws IOException {
    thrown.expect(StringIndexOutOfBoundsException.class);
    fxc.invertFXRates(new OHLCVTimeSeries(EMPTY, 1));
  }

  @Test
  public void invertExistingFXRatesWithoutComputing() throws IOException {
    final OHLCVTimeSeries usdGBP = EXPECTEDS.get(USDGBP);
    final OHLCVTimeSeries gbpUSD = EXPECTEDS.get(GBPUSD); // GBPUSD exists

    final OHLCVTimeSeries actual = fxc.invertFXRates(usdGBP);
    assertNotSame(gbpUSD, actual);
    compare(gbpUSD, actual);
  }

  @Test
  public void invertFXRates() throws IOException {
    final OHLCVTimeSeries usdGBP = EXPECTEDS.get(USDGBP);
    final OHLCVTimeSeries gbpUSD = EXPECTEDS.get(GBPUSD);

    compare(usdGBP, fxc.invertFXRates(gbpUSD));
  }

  @Test
  public void invertFXRates2() throws InterruptedException{
    final OHLCVTimeSeries usdGBP = EXPECTEDS.get(USDGBP);
    final OHLCVTimeSeries gbpUSD = EXPECTEDS.get(GBPUSD);
    final OHLCVTimeSeries usdAUD = EXPECTEDS.get(USDAUD);
    final OHLCVTimeSeries audUSD = EXPECTEDS.get(AUDUSD);

    final List<OHLCVTimeSeries> actuals = fxc.invertFXRates(Arrays.asList(usdGBP, usdAUD, null));
    compare(gbpUSD, actuals.get(0));
    compare(audUSD, actuals.get(1));
    assertEquals(0, actuals.get(2).size());
  }

  @Test
  public void operators() {
    assertEquals(DIVIDE, valueOf("DIVIDE"));
    assertEquals(MULTIPLY, valueOf("MULTIPLY"));
    assertEquals(INVERSE_MULTIPLE, valueOf("INVERSE_MULTIPLE"));
    assertEquals(3, values().length);
  }

  @Test
  public void operatorDivide() {
    final double left = Math.random();
    final double right = Math.random();
    assertEquals((left / right), DIVIDE.operate(left, right), 0.0d);
  }

  @Test
  public void operatorMultiply() {
    final double left = Math.random();
    final double right = Math.random();
    assertEquals((left * right), MULTIPLY.operate(left, right), 0.0d);
  }

  @Test
  public void operatorInverseMultiple() {
    final double left = Math.random();
    final double right = Math.random();
    assertEquals(1 / (left * right), INVERSE_MULTIPLE.operate(left, right), 0.0d);
  }

  private static final void compare(final OHLCVTimeSeries expected,
                                    final OHLCVTimeSeries actual) {
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); ++i) {
      assertEquals("Date: " + i, expected.date(i), actual.date(i));
      assertEquals("Open: " + i, expected.open(i), actual.open(i), DELTA);
      assertEquals("High: " + i, expected.high(i), actual.high(i), DELTA);
      assertEquals("Low: " + i, expected.low(i), actual.low(i), DELTA);
      assertEquals("Close: " + i, expected.close(i), actual.close(i), DELTA);
      assertEquals("Volume: " + i, expected.volume(i), actual.volume(i), DELTA);
    }
    assertEquals(expected.toString(), actual.toString());
  }

}
