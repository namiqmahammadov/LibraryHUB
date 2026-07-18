# LibraryHUB
 
## Layihənin işə salınması

### 1. Repository-ni klonlayın

```bash
git clone https://github.com/namiqmahammadov/LibraryHUB.git
cd LibraryHUB
```

### 2. `.env.example` faylını `.env` olaraq kopyalayın

```bash
cp .env.example .env
```

> Windows istifadəçiləri `.env.example` faylının adını `.env` olaraq dəyişə bilərlər.

### 3. Servisləri işə salın

```bash
docker compose up -d
```

### 4. Servislərin işlədiyini yoxlayın

```bash
docker compose ps
```
