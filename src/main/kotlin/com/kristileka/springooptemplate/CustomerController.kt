package com.kristileka.springooptemplate

import com.kristileka.springooptemplate.customer.Customer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class CustomerController(
    val customerRepository: CustomerRepository
) {
    @GetMapping
    fun getCustomers() = customerRepository.findAll().toList()

    @GetMapping("/add")
    fun addCustomer() {
        customerRepository.save(
            Customer(
                name = "Kristi",
            )
        )
    }

    @GetMapping("/deposit")
    fun deposit(): Customer? {
        val test = customerRepository.findAll().first()
        test.deposit(
            BigDecimal.TEN,
            Customer.Balance.Type.REAL,
        )
        customerRepository.save(test)
        return customerRepository.findAll().first()
    }

    @GetMapping("/withdraw")
    fun withdraw(): Customer? {
        val test = customerRepository.findAll().first()
        test.withdraw(
            BigDecimal.TEN,
            Customer.Balance.Type.REAL,
        )
        customerRepository.save(test)
        return customerRepository.findAll().first()
    }
    @GetMapping("/fake/deposit")
    fun fakeDeposit(): Customer? {
        val test = customerRepository.findAll().first()
        test.deposit(
            BigDecimal.TEN,
            Customer.Balance.Type.FAKE,
        )
        customerRepository.save(test)
        return customerRepository.findAll().first()
    }

    @GetMapping("/fake/withdraw")
    fun fakeWithdraw(): Customer? {
        val test = customerRepository.findAll().first()
        test.withdraw(
            BigDecimal.TEN,
            Customer.Balance.Type.FAKE,
        )
        customerRepository.save(test)
        return customerRepository.findAll().first()
    }
}