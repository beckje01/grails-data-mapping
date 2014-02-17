package grails.gorm.tests

/**
 * Created by jeff.beck on 1/27/14.
 */
class CommonTypes implements Serializable {
	UUID id
	Long version
	Long l
	Byte b
	Short s
	Boolean bool
	Integer i
	URL url
	Date date
	Calendar c
	BigDecimal bd
	BigInteger bi
	Double d
	Float f
	TimeZone tz
	Locale loc
	Currency cur
	byte[] ba
}
