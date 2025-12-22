Secure secrets guidance for this project

This project reads the Airtable token from the environment variable `AIRTABLE_TOKEN`.

Local (PowerShell) - one-time run:
```powershell
$env:AIRTABLE_TOKEN = 'your_new_token_here'
.\mvnw.cmd spring-boot:run
```

Local (PowerShell) - set persistently for your user:
```powershell
setx AIRTABLE_TOKEN "your_new_token_here"
# Restart your terminal/IDE for the variable to take effect
```

Run tests with the token for a single command:
```powershell
$env:AIRTABLE_TOKEN='your_new_token_here'; .\mvnw.cmd test
```

Docker run example:
```powershell
docker run -e AIRTABLE_TOKEN='your_new_token_here' your-image
```

GitHub Actions - set repository secret:
1. Go to your repository -> Settings -> Secrets -> Actions -> New repository secret
2. Create a secret named `AIRTABLE_TOKEN` with the token value

Then in your workflow YAML:
```yaml
name: CI
on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    env:
      AIRTABLE_TOKEN: ${{ secrets.AIRTABLE_TOKEN }}
    steps:
      - uses: actions/checkout@v4
      - name: Build and test
        run: mvn -B test
```

Notes:
- Rotate the token immediately if it was exposed in the repository.
- Never log or print the token in logs.
- For production, prefer using a managed secret store (AWS Secrets Manager, Azure Key Vault, GCP Secret Manager, or HashiCorp Vault) and inject the secret at runtime.

