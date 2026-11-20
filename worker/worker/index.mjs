// Cloudflare Workers attend un export par défaut, que Kotlin/JS ne sait pas produire.
import { fetch, initDictionary } from "../build/dist/js/productionLibrary/minitus-worker.mjs";

// Importé comme module texte (voir les règles de wrangler.toml), puis chargé une seule fois
// par isolate, au démarrage, plutôt qu'à chaque requête.
import wordList from "../build/worker/words.txt";

initDictionary(wordList);

export default { fetch };
