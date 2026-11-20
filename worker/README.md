# Minitus sur Cloudflare Workers

Ce module distribue Minitus via [Cloudflare Workers](https://developers.cloudflare.com/workers/),
à l'aide de Kotlin/JS et du module `core-cloudflare` de MiniPavi-Kotlin.

## Fonctionnement

- La logique du jeu vit dans le module `:shared`, commun à la JVM et à JS.
- À la compilation, la tâche Gradle `:worker:generateWordList` normalise, filtre et trie le
  dictionnaire, et écrit le résultat dans `build/worker/words.txt`.
- [`worker/index.mjs`](worker/index.mjs) importe cette liste comme module texte, la charge une
  seule fois au démarrage de l'isolate via `initDictionary()`, puis réexporte le gestionnaire
  `fetch` en tant qu'export par défaut, comme l'attend Cloudflare Workers.
- Le service répond en `POST /` (passerelle MiniPavi) et en `GET /status`.

## Développement

Il faut un JDK 21, Node.js 22, et un jeton GitHub avec la portée `read:packages` pour récupérer
MiniPavi-Kotlin depuis GitHub Packages (voir `settings.gradle.kts`).

```shell
npm ci
npm run dev     # compile le service et lance un Worker local sur http://localhost:8787
```

## Déploiement

```shell
npx wrangler login
npm run deploy  # compile le service et le déploie sur votre compte Cloudflare
```

Le workflow GitHub Actions `Deploy Cloudflare Worker` fait la même chose à chaque push sur
`main`. Il a besoin de :

1. Dans **Settings → Environments**, créez un environnement `production`. Vous pouvez y
   limiter les branches de déploiement à `main`, ou exiger une validation manuelle.
2. Ajoutez-y les secrets d'environnement `CLOUDFLARE_API_TOKEN` (jeton d'API avec la
   permission « Workers Scripts: Edit ») et `CLOUDFLARE_ACCOUNT_ID`.

Le workflow peut aussi être lancé à la main depuis l'onglet Actions.

Une fois déployé, faites pointer le service MiniPavi vers l'URL du Worker
(par exemple `https://minitus.<compte>.workers.dev/`).
