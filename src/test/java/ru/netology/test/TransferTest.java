package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardNumber;
import static ru.netology.data.DataHelper.getSecondCardNumber;
import static ru.netology.page.DashboardPage.pushFirstCardButton;
import static ru.netology.page.DashboardPage.pushSecondCardButton;

public class TransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
    }
    // перевод с первой карты на вторую
    @Test
    public void shouldTransferFrom1To2() {
        int amount = 7000;
        var cardBalance = new DashboardPage();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        var firstCardBalanceFinish = firstCardBalanceStart - amount;
        var secondCardBalanceFinish = secondCardBalanceStart + amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());
    }
    // перевод со второй карты на первую
    @Test
    public void shouldTransferFrom2To1() {
        int amount = 7000;
        var cardBalance = new DashboardPage();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = pushFirstCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        var firstCardBalanceFinish = firstCardBalanceStart + amount;
        var secondCardBalanceFinish = secondCardBalanceStart - amount;
        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());
    }
    //перевод суммы, превышающей баланс
    @Test
    public void shouldTransferMoreBalance() {
        int amount = 11000;
        var cardBalance = new DashboardPage();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.errorLimit();
    }
    //перевод на туже карту с которой переводим
    @Test
    public void shouldTransferFrom2To2Card() {
        int amount = 1000;
        var cardBalance = new DashboardPage();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        transactionPage.invalidCard();
    }
}