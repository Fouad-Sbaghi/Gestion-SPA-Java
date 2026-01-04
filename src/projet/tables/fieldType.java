package projet.tables;

/**
 * Énumération des types de données SQL supportés.
 * <p>
 * Utilisée par l'interface ITable pour définir le mapping
 * entre les colonnes SQL et les attributs Java.
 * </p>
 */
public enum fieldType {
	/** Nombre entier (INTEGER) */
	NUMERIC,
	/** Chaîne de caractères (VARCHAR) */
	VARCHAR,
	/** Nombre décimal (FLOAT) */
	FLOAT8,
	/** Entier 4 octets (INT4) */
	INT4;
}
