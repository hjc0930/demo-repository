package config

import (
	"os"
	"strconv"
)

type Config struct {
	Port string
	DB   DBConfig
}

type DBConfig struct {
	Driver   string // "mysql" or "sqlite"
	Host     string
	Port     string
	User     string
	Password string
	DBName   string
}

func Load() *Config {
	return &Config{
		Port: getEnv("SERVER_PORT", "8080"),
		DB: DBConfig{
			Driver:   getEnv("DB_DRIVER", "sqlite"),
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "3306"),
			User:     getEnv("DB_USER", "root"),
			Password: getEnv("DB_PASSWORD", "123456"),
			DBName:   getEnv("DB_NAME", "todo_db"),
		},
	}
}

// SQLiteDSN returns the SQLite file path, defaulting to "todo.db".
func (c *DBConfig) SQLiteDSN() string {
	if c.DBName == "" {
		return "todo.db"
	}
	return c.DBName + ".db"
}

// DSN returns the MySQL data source name for GORM.
func (c *DBConfig) DSN() string {
	return c.User + ":" + c.Password +
		"@tcp(" + c.Host + ":" + c.Port + ")/" +
		c.DBName +
		"?charset=utf8mb4&parseTime=True&loc=Local"
}

func getEnv(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}

func getEnvInt(key string, fallback int) int {
	if v := os.Getenv(key); v != "" {
		if n, err := strconv.Atoi(v); err == nil {
			return n
		}
	}
	return fallback
}

// GetenvInt is a public wrapper for tests.
func GetenvInt(key string, fallback int) int {
	return getEnvInt(key, fallback)
}
