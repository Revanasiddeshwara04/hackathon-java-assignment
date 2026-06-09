# Questions

Here are 2 questions related to the codebase. There's no right or wrong answer - we want to understand your reasoning.

## Question 1: API Specification Approaches

When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded everything directly. 

What are your thoughts on the pros and cons of each approach? Which would you choose and why?

**Answer:**
OpenAPI-generated APIs provide a contract-first approach where the API specification
acts as the single source of truth.

This improves consistency, documentation, client generation,
and collaboration between teams.

However, it adds an extra code-generation step and can reduce
flexibility when making quick changes.

Hand-coded APIs are simpler to start with and offer greater flexibility
for implementation.

They are suitable for smaller or internal services but can lead to
inconsistencies between documentation and implementation over time.

For this project, I would use the OpenAPI-generated approach for business-critical
APIs such as Warehouse management because it provides stronger contract enforcement
and maintainability.

For smaller internal services, hand-coded endpoints can be sufficient and allow
faster development.



---

## Question 2: Testing Strategy

Given the need to balance thorough testing with time and resource constraints, how would you prioritize tests for this project? 

Which types of tests (unit, integration, parameterized, etc.) would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**

Given limited time and resources, I would prioritize tests based on business risk.

First, I would focus on unit tests for domain use cases because they are fast,
reliable, and validate business rules.

Next, I would add integration tests to verify database interactions,
transactions, and repository behavior.

For critical operations involving concurrent updates,
I would include concurrency and optimistic locking tests.

Parameterized tests would be used for validation scenarios
to reduce duplication and improve coverage.

My testing priority would be:

1. Unit Tests
2. Integration Tests
3. Concurrency and Optimistic Locking Tests
4. Parameterized Tests

To maintain effective coverage over time,
all tests should run automatically in CI/CD pipelines,
and new features should include corresponding test cases
before being merged.

This approach provides a good balance between confidence,
execution speed, and long-term maintainability.