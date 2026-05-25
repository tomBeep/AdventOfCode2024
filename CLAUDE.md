## Java Code Style

### Type Declarations
- **CRITICAL: Never use `var` keyword** - Always use explicit type declarations
    - ❌ Bad: `var username = getValue();`
    - ✅ Good: `String username = getValue();`