# 🌐 Web Crawler for Information Retrieval

A Python-based web crawler designed to fetch, process, and index web content for information retrieval tasks. This tool can crawl a seed URL, extract and tokenize textual content, remove stopwords, and store clean data for further analysis.

## 🚀 Features

* ✅ **Recursive Crawling**: Follows and extracts links up to a specified depth from a seed URL
* 🧠 **Text Processing**:

  * Removes HTML tags and scripts
  * Tokenizes text using NLTK
  * Removes English stopwords
* 📦 **Data Storage**: Stores the processed data in CSV format for downstream tasks
* 🔍 **IR-Ready**: Cleaned and structured data can be used for building inverted indices, search engines, and IR models

## 🛠️ Tech Stack

* Python 3.x
* `requests`
* `BeautifulSoup`
* `nltk`
* `pandas`
* `urllib.parse`

## 📁 Project Structure

```plaintext
Web-Crawler_IR/
│
├── web_crawler.py          # Main script for crawling and processing
├── urls.csv                # Collected URLs (output)
├── clean_tokens.csv        # Preprocessed tokens (output)
├── stopwords.txt           # List of stopwords (optional file)
└── README.md               # Project overview and instructions
```

## ⚙️ How It Works

1. **Start Crawling**: Begins from a user-defined seed URL.
2. **Extract Content**: Grabs all visible text while ignoring scripts and styles.
3. **Preprocess Text**:

   * Normalize and tokenize
   * Filter out stopwords
4. **Save Output**: Stores URLs and cleaned tokens in CSV files.

## 🧪 Usage

1. **Install Dependencies**:

   ```bash
   pip install requests beautifulsoup4 nltk pandas
   ```

2. **Download NLTK Resources (once)**:

   ```python
   import nltk
   nltk.download('punkt')
   nltk.download('stopwords')
   ```

3. **Run the Script**:

   ```bash
   python web_crawler.py
   ```

4. **Check Output**:

   * `urls.csv`: List of crawled URLs
   * `clean_tokens.csv`: Tokenized, stopword-removed content

## 💡 Use Cases

* Building an **inverted index** for search engines
* Analyzing web content for NLP tasks
* Educational tool for understanding crawling and IR pipelines

## 📌 Notes

* Make sure the URLs you crawl allow scraping (check `robots.txt`)
* Consider limiting crawl depth and rate to avoid overwhelming servers

## 📬 Contact

Made with ❤️ by [@AbdooMatrix](https://github.com/AbdooMatrix) and other team members.
Feel free to open an issue or pull request for suggestions or improvements!

