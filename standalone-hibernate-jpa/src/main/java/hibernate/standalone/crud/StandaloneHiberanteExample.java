package hibernate.standalone.crud;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

public class StandaloneHiberanteExample {

	public static void main(String[] args) {
		System.out.println("Maven + Hibernate + HSQL");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		StandaloneHiberanteExample app = new StandaloneHiberanteExample();
		app.saveHotel("GTA", "LON");
		app.saveHotel("DI", "PMI");
		app.saveHotel("EXPE01", "LON");
		app.saveHotel("MTS02", "PMI");
		//app.listHotel();
		app.listNonEanMTS();
	}

	public Long saveHotel(String supplierCode, String supplierCity) {
		Session session = null;
		Long hotelId = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Hotel hotel = new Hotel();
			hotel.setSupplierCode(supplierCode);
			hotel.setSupplierCity(supplierCity);
			hotelId = (Long) session.save(hotel);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return hotelId;
	}

	public void listHotel() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<Hotel> hotelList = session.createQuery("from Hotel").list();
			for (Iterator<Hotel> iterator = hotelList.iterator(); iterator
					.hasNext();) {
				Hotel hotel = (Hotel) iterator.next();
				System.out.println(hotel.toString());
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void listNonEanMTS(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			Criteria criteria = session.createCriteria(Hotel.class);
	        criteria.add(Expression.not(Expression.and(Expression.eq("supplierCode","EXPE01"), Expression.eq("supplierCity","LON"))));
			criteria.add(Expression.not(Expression.and(Expression.eq("supplierCode","MTS02"), Expression.eq("supplierCity","PMI"))));        			
			List<Hotel> hotelList = criteria.list();
			for (Iterator<Hotel> iterator = hotelList.iterator(); iterator
					.hasNext();) {
				Hotel hotel = (Hotel) iterator.next();
				System.out.println(hotel.toString());
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}		
	}

}
