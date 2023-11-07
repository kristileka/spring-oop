package com.kristileka.springooptemplate.customer

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity(name = "customer")
data class Customer(
    @Id val id: UUID = UUID.randomUUID(),
    val name: String,

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], orphanRemoval = true)
    @MapKey(name = "type")
    var balance: MutableMap<Balance.Type, Balance> = mutableMapOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], orphanRemoval = true)
    var operations: MutableList<Balance.Operation> = mutableListOf()
) {
    @Entity(name = "customer_balance")
    data class Balance(
        @Id
        val id: UUID = UUID.randomUUID(),

        @Enumerated(EnumType.STRING)
        val type: Type,

        var amount: BigDecimal,

        @OneToMany(mappedBy = "balance", cascade = [CascadeType.ALL], orphanRemoval = true)
        @JsonIgnore
        val operations: List<Operation> = listOf(),

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        val customer: Customer
    ) {
        enum class Type {
            REAL, FAKE
        }

        @Entity(name = "customer_balance_operation")
        data class Operation(
            @Id
            val id: UUID = UUID.randomUUID(),

            @Enumerated(EnumType.STRING)

            val type: Type,

            val amount: BigDecimal,
            @ManyToOne(fetch = FetchType.LAZY)
            @JsonIgnore

            val customer: Customer,

            @ManyToOne(fetch = FetchType.LAZY)
            @JsonIgnore
            val balance: Balance
        ) {
            enum class Type {
                INCREASE,
                DECREASE
            }
        }
    }

    private fun initializeBalance() {
        balance[Balance.Type.REAL] = Balance(
            type = Balance.Type.REAL,
            customer = this,
            amount = BigDecimal.ZERO
        )

        balance[Balance.Type.FAKE] = Balance(
            type = Balance.Type.FAKE,
            customer = this,
            amount = BigDecimal.ZERO
        )
    }

    fun deposit(
        amount: BigDecimal,
        balanceType: Balance.Type
    ) {
        if (this.balance.isEmpty())
            initializeBalance()
        val balance = this.balance[balanceType] ?: throw RuntimeException()
        balance.amount = this.balance[balanceType]?.amount?.minus(amount)!!
        this.operations.add(
            Balance.Operation(
                type = Balance.Operation.Type.INCREASE,
                amount = amount,
                customer = this,
                balance = balance
            )
        )
    }

    fun withdraw(
        amount: BigDecimal,
        balanceType: Balance.Type
    ) {
        if (this.balance.isEmpty())
            initializeBalance()
        val balance = this.balance[balanceType] ?: throw RuntimeException()
        balance.amount = this.balance[balanceType]?.amount!!.plus(amount)
        this.operations.add(
            Balance.Operation(
                type = Balance.Operation.Type.INCREASE,
                amount = amount,
                customer = this,
                balance = balance
            )
        )
    }
}